package com.sonnguyen.base.controller;

import com.sonnguyen.base.dto.req.UserReq;
import com.sonnguyen.base.dto.res.ApiResponse;
import com.sonnguyen.base.model.User;
import com.sonnguyen.base.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody @Validated UserReq user) {
        User createdUser = userService.createUser(user);
        ApiResponse<User> response = ApiResponse.<User>builder()
                .success(true)
                .message("User created successfully")
                .data(createdUser)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        ApiResponse<List<User>> response = ApiResponse.<List<User>>builder()
                .success(true)
                .message("Fetched users successfully")
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable String id) {
        ApiResponse<User> response = ApiResponse.<User>builder()
                .success(true)
                .message("Fetched user successfully")
                .data(userService.getUserById(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable String id, @RequestBody @Valid UserReq user) {
        User updatedUser = userService.updateUser(id, user);
        ApiResponse<User> response = ApiResponse.<User>builder()
                .success(true)
                .message("User updated successfully")
                .data(updatedUser)
                .build();
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .success(false)
                    .message("User not found")
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("User deleted successfully")
                .data("User with ID " + id + " deleted")
                .build();
        return ResponseEntity.ok(response);
    }
}