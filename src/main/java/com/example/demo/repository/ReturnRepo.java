package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BorrowCardDetail;
import com.example.demo.model.BorrowCardDetailId;
@Repository
public interface ReturnRepo extends JpaRepository<BorrowCardDetail, BorrowCardDetailId> {
    
}
