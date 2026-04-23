package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Reader;

import jakarta.transaction.Transactional;

@Repository
public interface ReaderRepo extends JpaRepository<Reader, Integer> {
    @Query(value = """
            SELECT COUNT(*) FROM borrow_card_detail AS cd 
            JOIN borrow_card AS bc ON bc.borrow_card_id = cd.borrow_card_id
            WHERE bc.reader_id = ?1 AND cd.status = 'borrowing';
            """, nativeQuery = true)
    int getQtyBorrowing(int readerId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO borrow_card(user_id, reader_id, total_amount, note, created_at) VALUES(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void createCard(int userId, int readerId, BigDecimal totalAmount, String note, LocalDateTime createdAt);

    @Query(value = "SELECT borrow_card_id FROM borrow_card WHERE reader_id = ?1 ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    String getCardCreatedId(int readerId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO borrow_card_detail(borrow_card_id, book_id, expire, status) VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
    void addDetailCard(int borrowCardId, int bookId, LocalDateTime expire, String status);

    Optional<Reader> findByEmail(String email);
}
