package com.example.demo.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Book;

import jakarta.transaction.Transactional;

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

    @Query(value = "SELECT COUNT(*) FROM borrow_card_detail WHERE book_id = ?1", nativeQuery = true)
    Integer checkBookInCard(int bookId);

    @Query(value = "SELECT quantity FROM book WHERE book_id = ?1", nativeQuery = true)
    Integer findQty(int book_id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE book SET quantity = ?1, name = ?2, description = ?3, author = ?4, publish = ?5, type = ?6, url_img = ?7 WHERE book_id = ?8", nativeQuery = true)
    void updateBook(int quantity, String name, String description, String author, String publish, String type,
            String urlImg, int bookId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO book(name, description, author, publish, quantity, url_img, type) VALUES(?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
    void addBook(String name, String description, String author, String publish, int quantity,
            String urlImg, String type);
}
