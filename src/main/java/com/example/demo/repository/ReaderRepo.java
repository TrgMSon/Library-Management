package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Reader;

@Repository
public interface ReaderRepo extends JpaRepository<Reader, Integer> {
    @Query(value = """
            SELECT COUNT(*) FROM borrow_card_detail AS cd 
            JOIN borrow_card AS bc ON bc.borrow_card_id = cd.borrow_card_id
            WHERE bc.reader_id = ?1 AND cd.status = 'borrowing';
            """, nativeQuery = true)
    int getQtyBorrowing(int readerId);

    Optional<Reader> findByEmail(String email);
}
