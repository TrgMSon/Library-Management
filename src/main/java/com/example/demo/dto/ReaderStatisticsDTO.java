package com.example.demo.dto;

public class ReaderStatisticsDTO {
    private Integer readerId;
    private String name;
    private String email;
    private Long borrowCount;
    
    public ReaderStatisticsDTO(Integer readerId, String name, String email, Long borrowCount) {
        this.readerId = readerId;
        this.name = name;
        this.email = email;
        this.borrowCount = borrowCount;
    }
    
    // Getters and Setters
    public Integer getReaderId() { return readerId; }
    public void setReaderId(Integer readerId) { this.readerId = readerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Long getBorrowCount() { return borrowCount; }
    public void setBorrowCount(Long borrowCount) { this.borrowCount = borrowCount; }
}