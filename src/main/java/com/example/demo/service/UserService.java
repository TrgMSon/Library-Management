package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User findUserById(String id, String password) {
        User result = userRepo.findById(id, password);
        return result;
    }


}
