package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="borrow_card")
@Data
@NoArgsConstructor
public class BorrowCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="borrow_card_id")
    private int borrowCardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reader_id")
    private Reader reader;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="total_amount")
    private BigDecimal totalAmount;

    private String note;
}
