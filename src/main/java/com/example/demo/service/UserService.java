package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.UserDTO;
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

    public ResponseEntity<String> addUser(UserDTO userDTO) {
        String name = userDTO.getName();
        String email = userDTO.getEmail();
        if (userRepo.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email người dùng đã tồn tại, vui lòng nhập email khác!");
        }

        String password = userDTO.getPassword();
        String role = userDTO.getRole();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        userRepo.save(user);
        return ResponseEntity.ok().body("Thêm người dùng thành công!");
    }

    public ResponseEntity<String> updateUser(User updatedUser) {
        User employee = userRepo.findById(updatedUser.getUserId()).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        String oldEmail = employee.getEmail();
        String newEmail = updatedUser.getEmail();

        if (!oldEmail.equals(newEmail) && userRepo.existsByEmail(newEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email người dùng đã tồn tại, vui lòng nhập email khác!");
        }

        employee.setEmail(updatedUser.getEmail());
        employee.setRole(updatedUser.getRole());
        employee.setName(updatedUser.getName());

        userRepo.save(employee);
        return ResponseEntity.ok("Cập nhật thành công!");
    }

    public ResponseEntity<String> deleteUser(int userId) {
        if (borrowCardRepo.existsBorrowCardByUser_UserId(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Người dùng đã từng tạo phiếu mượn, không thể xoá!");
        }

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User không tồn tại");
        }

        userRepo.delete(user);
        return ResponseEntity.ok("Xoá người dùng thành công!");
    }
}
