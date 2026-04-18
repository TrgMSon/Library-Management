package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/manage_user")
    public String openUserManagePage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        User currUser = userService.findUserById(Integer.parseInt(userId));
        if (currUser == null) {
            return "redirect:/login";
        }

        if (!"admin".equals(currUser.getRole())) {
            return "redirect:/home";
        }

        List<User> userList = userRepo.findAll();
        if (userList.isEmpty()) {
            model.addAttribute("msg", "Chưa có người dùng!");
            return "manage-user";
        }

        model.addAttribute("user_list", userList);
        return "manage-user";
    }

}