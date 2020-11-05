package com.ifv.demo.service;

import com.ifv.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {
    public UserDetails loadUserById(Long id);
    public void save(User user);

    public Optional<User> findById(Long id);

    public User findByUsername(String username);

}
