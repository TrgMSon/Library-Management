package com.example.demo.controller;

import com.example.demo.model.Reader;
import com.example.demo.model.User;
import com.example.demo.repository.ReaderRepo;
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
public class ReaderController {
    @Autowired
    private ReaderRepo readerRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/manage_reader")
    public String openReaderManagementPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<Reader> readerList = readerRepo.findAll();
        if (readerList.isEmpty()) {
            model.addAttribute("msg", "Chưa có độc giả nào!");
            return "manage-reader";
        }

        model.addAttribute("reader_list", readerList);

        User user = userService.findUserById(Integer.parseInt(userId));
        model.addAttribute("role", user.getRole());
        return "manage-reader";
    }
}