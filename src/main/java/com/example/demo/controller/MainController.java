package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String checkLogin(@ModelAttribute User user, RedirectAttributes ra, HttpSession session) {
        String id = user.getUserId().trim();
        String password = user.getPassword();

        if (id.equals("")) return "redirect:/login";

        User result = userService.findUserById(id, password);

        if (result != null) {
            session.setAttribute("userId", user.getUserId());
            return "redirect:/home";
        }
        else {
            ra.addFlashAttribute("message", "ID hoặc mật khẩu không đúng, vui lòng thử lại.");
            return "redirect:/login";
        }
    }

    @GetMapping("/home")
    public String showHome() {
        return "home";
    }
}