package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Reader;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BorrowCard;
import com.example.demo.model.BorrowCardDetail;

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
}
