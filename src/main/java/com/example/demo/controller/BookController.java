package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.dto.BookDeleteDTO;
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
    public ArrayList<Book> getAllBook() {
        return bookService.getAllBook();
    }

    @GetMapping("/searchBookType")
    public ArrayList<Book> searchBookType(@RequestParam String type, @RequestParam String name) {
        return bookService.searchBookType(type, name);
    }

    @GetMapping("/searchBookName")
    public ArrayList<Book> searchBookByName(@RequestParam String name) {
        return bookService.searchBookByName(name);
    }

    @GetMapping("/searchBookId")
    public Book searchBookById(@RequestParam int bookId) {
        return bookService.searchBookById(bookId);
    }
    
    @PostMapping("/deleteBook")
    public boolean deleteBook(@RequestBody BookDeleteDTO bookDeleteDTO) {
        if (bookService.checkBookInCard(bookDeleteDTO.getBookId())) return false;
        bookService.deleteBook(bookDeleteDTO.getBookId());
        return true;
    } 

    @PostMapping("/updateBook")
    public void updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    @PostMapping("/addBook")
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }
}
