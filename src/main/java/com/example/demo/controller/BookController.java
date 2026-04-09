package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/viewBook")
    public Book viewBook(@RequestParam int bookId) {
        return bookService.findBookById(bookId);
    }

    @GetMapping("/getBookType")
    public ArrayList<Book> getBookType(@RequestParam String type) {
        return bookService.getAllBookType(type);
    }

    @GetMapping("/getAllBook")
    public ArrayList<Book> getPagingBook() {
        return bookService.getAllBook();
    }
}
