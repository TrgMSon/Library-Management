package com.example.demo.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Book;

public interface BookRepo extends JpaRepository<Book, Integer> {
    @Query(value = "SELECT * FROM book WHERE type=?1", nativeQuery = true)
    ArrayList<Book> findAllBookType(String type);

    @Query(value = "SELECT * FROM book WHERE book_id = ?1", nativeQuery = true)
    Book findBookById(int bookId);

    @Query(value = "SELECT * FROM book", nativeQuery = true)
    ArrayList<Book> findAllBook();

    @Query(value = "SELECT * FROM book WHERE type=?1 AND name LIKE ?2", nativeQuery = true)
    ArrayList<Book> findBookByNameAndType(String type, String name);

    @Query(value = "SELECT * FROM book WHERE name LIKE ?1", nativeQuery = true)
    ArrayList<Book> findBookByName(String name);
}
