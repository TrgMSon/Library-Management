package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepo;

@Service
public class BookService {
    @Autowired
    private BookRepo bookRepo;

    public ArrayList<Book> getAllBookType(String type) {
        ArrayList<Book> books = new ArrayList<>();
        books = bookRepo.findAllBookType(type);
        return books;
    }

    public Book findBookById(int id) {
        return bookRepo.findBookById(id);
    }

    public ArrayList<Book> getAllBook() {
        return bookRepo.findAllBook();
    }

    public ArrayList<Book> searchBookType(String type, String name) {
        return bookRepo.findBookByNameAndType(type, "%" + name + "%");
    }

    public ArrayList<Book> searchBook(String name) {
        return bookRepo.findBookByName("%" + name + "%");
    }
}
