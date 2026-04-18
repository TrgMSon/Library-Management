package com.example.demo.dto;

public class BookStatisticsDTO {
    private Integer bookId;
    private String name;
    private String author;
    private Long borrowCount;
    
    // Constructor đầy đủ
    public BookStatisticsDTO(Integer bookId, String name, String author, Long borrowCount) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.borrowCount = borrowCount;
    }
    
    // Constructor mặc định (bắt buộc phải có)
    public BookStatisticsDTO() {
    }
    
    // Getters and Setters
    public Integer getBookId() {
        return bookId;
    }
    
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public Long getBorrowCount() {
        return borrowCount;
    }
    
    public void setBorrowCount(Long borrowCount) {
        this.borrowCount = borrowCount;
    }
}