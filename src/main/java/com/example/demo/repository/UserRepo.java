package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

import jakarta.transaction.Transactional;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = """
            SELECT * FROM user WHERE email=?1 AND password=?2
            """, nativeQuery = true)
    User findByEmail(String email, String password);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user(email, name, password, role) VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
    void save(String email, String name, String password, String role);

    Optional<User> findByEmail(String email);
}
