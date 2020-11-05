package com.ifv.demo.controller;

import com.ifv.demo.jwt.CustomUserDetails;
import com.ifv.demo.jwt.JwtTokenProvider;
import com.ifv.demo.jwt.LoginRequest;
import com.ifv.demo.jwt.LoginResponse;
import com.ifv.demo.model.User;
import com.ifv.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class WebController {
    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse authencation(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new LoginResponse(jwt);
    }

    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody User user) {
        Map response = new HashMap();
        Optional<User> user1 = Optional.ofNullable(userService.findByUsername(user.getUsername()));
        if (user1.isPresent()) {
            response.put("status", HttpStatus.NOT_FOUND);
            response.put("message", "Tài khoản đã tồn tại");
            return response;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        response.put("status", HttpStatus.OK);
        response.put("message", "Đăng kí thành công");
        return response;

    }


    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

}
