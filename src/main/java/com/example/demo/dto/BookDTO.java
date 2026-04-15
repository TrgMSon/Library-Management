package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {
    private int bookId;
    private String name;
    private String author;
    private String description;
    private String publish;
    private String type;
    private int quantity;
    private String urlImg;
}
