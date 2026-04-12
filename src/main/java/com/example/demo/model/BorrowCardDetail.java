package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="borrow_card_detail")
@Data
@NoArgsConstructor
public class BorrowCardDetail {
    @EmbeddedId
    private BorrowCardDetailId cartDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("borrowCardId")
    @JoinColumn(name="borrow_card_id", nullable=false)
    private BorrowCard card;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name="book_id", nullable=false)
    private Book book;

    private LocalDateTime expire;

    @Column(name="return_date")
    private LocalDateTime returnDate;

    private String status;
}
