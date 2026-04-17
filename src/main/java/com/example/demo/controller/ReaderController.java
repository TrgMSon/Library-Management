package com.example.demo.controller;

import com.example.demo.model.Reader;
import com.example.demo.model.User;
import com.example.demo.repository.ReaderRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReaderController {
    private final ReaderRepo readerRepo;

    @GetMapping("/manage_reader")
    public String openReaderManagementPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Reader> readerList = readerRepo.findAll();
        if (readerList.isEmpty()) {
            model.addAttribute("msg", "Chưa có độc giả nào!");
            return "manage-reader";
        }

        model.addAttribute("reader_list", readerList);
        return "manage-reader";
    }
}
