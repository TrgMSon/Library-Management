package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping(value = {"/login", "/"})
    public String showLogin() {
        return "login";
    }

    @PostMapping(value = {"/login", "/"})
    public String checkLogin(@ModelAttribute User user, RedirectAttributes ra, HttpSession session) {
        String email = user.getEmail().trim();
        String password = user.getPassword();

        if (email.equals("")) return "redirect:/login";

        User result = userService.findUserByEmail(email, password);

        if (result != null) {
            session.setAttribute("userId", result.getUserId() + "");
            session.setAttribute("role", result.getRole());
            session.setAttribute("loggedInUser", user);
            return "redirect:/home";
        }
        else {
            ra.addFlashAttribute("error", "Email hoặc mật khẩu không đúng, vui lòng thử lại.");
            return "redirect:/login";
        }

    }

    @GetMapping("/home")
    public String showHome(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }
        
        ArrayList<Book> books = bookService.getAllBookType("IT");
        model.addAttribute("books", books);

        User user = userService.findUserById(Integer.parseInt(userId));
        model.addAttribute("userName", user.getName());
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("role", user.getRole());

        return "home";
    }

    @GetMapping("/viewDetail")
    public String showDetail(@RequestParam int bookId, Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Book book = bookService.findBookById(bookId);
        model.addAttribute("book", book);

        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);

        return "detailBook";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}