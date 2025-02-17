package com.sonnguyen.base.service.impl;

import com.sonnguyen.base.config.JwtService;
import com.sonnguyen.base.dto.res.AuthResponse;
import com.sonnguyen.base.exception.CommonException;
import com.sonnguyen.base.model.CustomUserDetails;
import com.sonnguyen.base.model.Role;
import com.sonnguyen.base.model.User;
import com.sonnguyen.base.repository.RoleRepository;
import com.sonnguyen.base.repository.UserRepository;
import com.sonnguyen.base.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new AuthResponse(token);
    }

    @Override
    public void register(String username, String password) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            throw new CommonException("username exist", HttpStatus.CONFLICT);
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new CommonException("Default role not found", HttpStatus.INTERNAL_SERVER_ERROR));

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
    }

}
