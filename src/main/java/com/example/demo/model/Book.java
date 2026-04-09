package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id", nullable=false)
    private Integer bookId;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private String author;

    @Column(nullable=false)
    private String publish;

    @Column(nullable=false)
    private Integer quantity;

    @Column(name="url_img", nullable=false)
    private String urlImg;

    @Column(nullable=false)
    private String type;
}
