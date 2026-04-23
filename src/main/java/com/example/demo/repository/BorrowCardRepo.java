package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BorrowCard;
import com.example.demo.model.BorrowCardDetail;
import com.example.demo.model.Reader;
import com.example.demo.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface BorrowCardRepo extends JpaRepository<BorrowCard, Integer> {
        @Query(value = "SELECT * FROM borrow_card ORDER BY created_at DESC", nativeQuery = true)
        ArrayList<BorrowCard> findAllBorrowCards();

        @Query(value = "SELECT * FROM borrow_card_detail WHERE borrow_card_id = ?1", nativeQuery = true)
        ArrayList<BorrowCardDetail> findBorrowCardDetail(int borrowCardId);

        @Query(value = """
                        SELECT bc.* FROM borrow_card AS bc
                        WHERE bc.reader_id = ?1
                        AND (SELECT COUNT(*) FROM borrow_card_detail AS bd WHERE bd.borrow_card_id = bc.borrow_card_id AND status = 'borrowing') > 0
                        ORDER BY bc.created_at DESC
                        """, nativeQuery = true)
        ArrayList<BorrowCard> findBorrowingCard(int readerId);

        @Query(value = """
                        SELECT bc.* FROM borrow_card AS bc
                        WHERE bc.reader_id = ?1
                        AND (SELECT COUNT(*) FROM borrow_card_detail AS bd WHERE bd.borrow_card_id = bc.borrow_card_id AND status = 'borrowing') = 0
                        ORDER BY bc.created_at DESC
                        """, nativeQuery = true)
        ArrayList<BorrowCard> findReturnedCard(int readerId);

        @Query(value = "SELECT * FROM borrow_card WHERE reader_id = ?1 ORDER BY created_at DESC", nativeQuery = true)
        ArrayList<BorrowCard> findCardById(int readerId);

        List<BorrowCard> findByReader(Reader reader);

        List<BorrowCard> findByUser(User user);

        // Thống kê sách mượn nhiều theo khoảng thời gian
        @Query("SELECT br.book.bookId, br.book.name, br.book.author, COUNT(br.card.borrowCardId) as borrowCount " +
                        "FROM BorrowCardDetail br " +
                        "WHERE br.card.createdAt BETWEEN :startDate AND :endDate " +
                        "GROUP BY br.book.bookId, br.book.name, br.book.author " +
                        "ORDER BY borrowCount DESC")
        List<Object[]> findMostBorrowedBooks(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Thống kê độc giả mượn nhiều theo khoảng thời gian
        @Query("SELECT br.reader.readerId, br.reader.name, br.reader.email, COUNT(br.borrowCardId) as borrowCount " +
                        "FROM BorrowCard br " +
                        "WHERE br.createdAt BETWEEN :startDate AND :endDate " +
                        "GROUP BY br.reader.readerId, br.reader.name, br.reader.email " +
                        "ORDER BY borrowCount DESC")
        List<Object[]> findMostActiveReaders(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Kiểm tra có dữ liệu trong khoảng thời gian không
        @Query("SELECT COUNT(br) FROM BorrowCard br " +
                        "WHERE br.createdAt BETWEEN :startDate AND :endDate")
        long countByBorrowDateBetween(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

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
}
