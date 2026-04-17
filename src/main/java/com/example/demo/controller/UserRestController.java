package com.example.demo.controller;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        User currUser = (User) session.getAttribute("loggedInUser");
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập trước để sử dụng tính năng");
        }

        if (currUser.getRole() != null && currUser.getRole().equals("user")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hãy yêu cầu quyền truy cập từ admin");
        }

        String name = userDTO.getName();
        String email = userDTO.getEmail();
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
        User currUser = (User) session.getAttribute("loggedInUser");
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
        User currUser = (User) session.getAttribute("loggedInUser");
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
    public ResponseEntity<String> deleteUser(HttpSession session, @PathVariable("id") int id) {
        User currUser = (User) session.getAttribute("loggedInUser");
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập trước để sử dụng tính năng");
        }

        if (currUser.getRole() != null && currUser.getRole().equals("user")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hãy yêu cầu quyền truy cập từ admin");
        }


        userService.deleteUser(id);
        return ResponseEntity.ok().body("Xoá người dùng thành công!");
    }
}
