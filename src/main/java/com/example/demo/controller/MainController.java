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
            if (result.getRole().equals("user")) return "redirect:/home";
            else return "redirect:/manage";
        }
        else {
            ra.addFlashAttribute("error", "Email hoặc mật khẩu không đúng, vui lòng thử lại.");
            return "redirect:/login";
        }
    }

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String doSignup(@ModelAttribute User user, RedirectAttributes ra) {
        User tmp = userService.findUserByEmail(user.getEmail(), user.getPassword());
        if (tmp == null) {
            ra.addFlashAttribute("message", "Đăng ký thành công");
            userService.saveUser(user);
            return "redirect:/login";
        }
        else {
            ra.addFlashAttribute("error", "Tài khoản đã tồn tại");
            return "redirect:/signup";
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

        return "home";
    }

    @GetMapping("/viewDetail")
    public String showDetail(@RequestParam int bookId, Model model) {
        Book book = bookService.findBookById(bookId);
        model.addAttribute("book", book);
        return "detailBook";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/manage")
    public String manage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.findUserById(Integer.parseInt(userId));
        model.addAttribute("userName", user.getName());

        return "manage";
    }
}