package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookDTO;
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

    public ArrayList<Book> searchBookByName(String name) {
        return bookRepo.findBookByName("%" + name + "%");
    }

    public Book searchBookById(int bookId) {
        Optional<Book> result = bookRepo.findById(bookId);
        if (result.isEmpty()) return null;
        return result.get();
    }

    public void deleteBook(int bookId) {
        bookRepo.deleteById(bookId);
    }

    public boolean checkBookInCard(int bookId) {
        return bookRepo.checkBookInCard(bookId) > 0;
    }

    public void updateBook(Book book) {
        bookRepo.updateBook(book.getQuantity(), book.getName(), book.getDescription(), book.getAuthor(), book.getPublish(), book.getType(), book.getUrlImg(), book.getBookId());
    }

    public void addBook(BookDTO book) {
        bookRepo.addBook(book.getName(), book.getDescription(), book.getAuthor(), book.getPublish(), book.getQuantity(), book.getUrlImg(), book.getType());
    }

    public boolean isInStock(int bookId, int cartQty) {
        return cartQty <= bookRepo.findQty(bookId);
    }
}
