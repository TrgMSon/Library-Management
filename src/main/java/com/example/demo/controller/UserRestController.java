package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserRestController {
    private final UserRepo userRepo;
    private final UserService userService;

    @PostMapping("/add_user")
    public ResponseEntity<String> addUser(HttpSession session, @RequestBody UserDTO userDTO) {
        String userId = (String) session.getAttribute("userId");
        User currUser = userService.findUserById(Integer.parseInt(userId));
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập trước để sử dụng tính năng");
        }

        if (currUser.getRole() != null && currUser.getRole().equals("user")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hãy yêu cầu quyền truy cập từ admin");
        }

        String name = userDTO.getName();
        String email = userDTO.getEmail();
        if (userRepo.findByEmail(email).isPresent()) {
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

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(HttpSession session, @PathVariable int id) {
        String userId = (String) session.getAttribute("userId");
        User currUser = userService.findUserById(Integer.parseInt(userId));
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (currUser.getRole() != null && currUser.getRole().equals("user")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        User employee = userRepo.findById(id).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        employee.setPassword(null);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/update_user")
    public ResponseEntity<String> processUpdatingUser(HttpSession session, @RequestBody User updatedUser) {
        String userId = (String) session.getAttribute("userId");
        User currUser = userService.findUserById(Integer.parseInt(userId));
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập trước để sử dụng tính năng");
        }

        if (currUser.getRole() != null && currUser.getRole().equals("user")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hãy yêu cầu quyền truy cập từ admin");
        }

        User employee = userRepo.findById(updatedUser.getUserId()).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        employee.setEmail(updatedUser.getEmail());
        employee.setRole(updatedUser.getRole());
        employee.setName(updatedUser.getName());

        userRepo.save(employee);
        return ResponseEntity.ok("Cập nhật thành công!");
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(HttpSession session, @PathVariable("id") int id) {
        String userId = (String) session.getAttribute("userId");
        return userService.deleteUser(id, Integer.parseInt(userId));
    }
}
