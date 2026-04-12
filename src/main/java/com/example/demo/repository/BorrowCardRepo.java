package com.example.demo.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BorrowCard;
import com.example.demo.model.BorrowCardDetail;

@Repository
public interface BorrowCardRepo extends JpaRepository<BorrowCard, Integer> {
    @Query(value = "SELECT * FROM borrow_card", nativeQuery = true)
    ArrayList<BorrowCard> findAllBorrowCards();

    @Query(value = "SELECT * FROM borrow_card_detail WHERE borrow_card_id = ?1", nativeQuery = true)
    ArrayList<BorrowCardDetail> findBorrowCardDetail(int borrowCardId);

}
