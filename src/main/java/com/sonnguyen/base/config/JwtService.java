package com.sonnguyen.base.config;

import com.sonnguyen.base.model.CustomUserDetails;
import com.sonnguyen.base.model.User;
import com.sonnguyen.base.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.SECRET_KEY:a1dae4457f142c88547a17529caec2f12c8ba2cb4ccd599aa75cfd7e5b27752f9d9167d1349a30596931fd238239d6d17fb49a8f63e27fd45cf7c49a65d3fd75}")
    private String secretKey;
    @Value("${jwt.expirationMs:3600000}")
    private long expirationTime;
    private final UserRepository userRepository;

    // Generate JWT token for user
    public String generateToken(CustomUserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .claim("authorities", authorities)
                .setSubject(userDetails.getUsername())
                .setIssuer("sonnguyen.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    // Check if token has expired
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    // Get Authentication object from token
    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            UserDetails userDetails = new CustomUserDetails(user);
            return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return null;
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetail) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(userDetail.getUsername()) && validateToken(token));
    }

}