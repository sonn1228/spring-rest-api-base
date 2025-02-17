package com.sonnguyen.base.service;

import com.sonnguyen.base.dto.req.UserReq;
import com.sonnguyen.base.exception.CommonException;
import com.sonnguyen.base.repository.UserRepository;
import com.sonnguyen.base.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserReq userReq) {
        if (userRepository.existsByUsername(userReq.getUsername())) {
            throw new CommonException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(userReq.getUsername());
        user.setPassword(passwordEncoder.encode(userReq.getPassword()));

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new CommonException("User not found", HttpStatus.BAD_REQUEST));
    }

    public User updateUser(String id, UserReq userReq) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(userReq.getUsername());
            user.setPassword(userReq.getPassword());
            return userRepository.save(user);
        } else {
            throw new CommonException("User not found", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
