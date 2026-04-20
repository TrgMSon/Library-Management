package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.model.BorrowCard;
import com.example.demo.model.User;
import com.example.demo.repository.BorrowCardRepo;
import com.example.demo.repository.UserRepo;;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BorrowCardRepo borrowCardRepo;

    public User findUserByEmail(String email, String password) {
        User result = userRepo.findByEmailAndPassword(email, password);
        return result;
    }

    public User findUserById(int userId) {
        Optional<User> optional = userRepo.findById(userId);
        if (optional.isEmpty())
            return null;
        else
            return optional.get();
    }

    public void saveUser(User user) {
        userRepo.save(user.getEmail(), user.getName(), user.getPassword(), "user");
    }

    public ResponseEntity<?> deleteUser(int id, int userId) {
        User currUser = findUserById(userId);
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập trước để sử dụng tính năng");
        }

        if (currUser.getRole() != null && currUser.getRole().equals("user")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hãy yêu cầu quyền truy cập từ admin");
        }

        User user = userRepo.findById(id).orElse(null);
        assert user != null;

        List<BorrowCard> borrowCards = borrowCardRepo.findByUser(user);
        if (borrowCards.isEmpty()) {
            userRepo.delete(user);
            return ResponseEntity.ok().body("Xoá người dùng thành công!");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Không thể xoá, người dùng đang cho độc giả mượn sách");
    }
}
