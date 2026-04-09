package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User findUserByEmail(String email, String password) {
        User result = userRepo.findByEmail(email, password);
        return result;
    }

    public User findUserById(int userId) {
        Optional<User> optional = userRepo.findById(userId);
        if (optional.isEmpty()) return null;
        else return optional.get();
    }

    public void saveUser(User user) {
        userRepo.save(user.getEmail(), user.getName(), user.getPassword(), "user");
    }
}
