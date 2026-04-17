package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.BorrowCard;
import com.example.demo.repository.BorrowCardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepo userRepo;
    private final BorrowCardRepo borrowCardRepo;

    public User findUserByEmail(String email, String password) {
        User result = userRepo.findByEmail(email, password);
        return result;
    }

    public User findUserById(int userId) {
        Optional<User> optional = userRepo.findById(userId);
        if (optional.isEmpty()) return null;
        else return optional.get();
    }

    public void saveUser(User user) {
        userRepo.save(user.getEmail(), user.getName(), user.getPassword(), "user");
    }

    @Transactional
    public void deleteUser(int id) {
        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            List<BorrowCard> borrowCards = borrowCardRepo.findByUser(user);
            if (borrowCards != null) {
                for (BorrowCard borrowCard : borrowCards) {
                    borrowCard.setUser(null);
                }

                borrowCardRepo.saveAll(borrowCards);
                userRepo.delete(user);
            }
        }
    }
}
