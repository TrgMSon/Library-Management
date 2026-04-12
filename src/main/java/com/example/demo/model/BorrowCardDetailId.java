package com.example.demo.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class BorrowCardDetailId implements Serializable {
    @Column(name="borrow_card_id", nullable=false)
    private int borrowCardId;

    @Column(name="book_id", nullable=false)
    private int bookId;
}
