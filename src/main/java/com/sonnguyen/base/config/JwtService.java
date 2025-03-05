package com.sonnguyen.base.config;

import com.sonnguyen.base.model.CustomUserDetails;
import com.sonnguyen.base.model.User;
import com.sonnguyen.base.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.SECRET_KEY}")
    private String secretKeyRaw;

    @Value("${jwt.expirationMs:3600000}")
    private long expirationTime;

    private final UserRepository userRepository;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyRaw));
    }

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
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
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
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(user -> {
            UserDetails userDetails = new CustomUserDetails(user);
            return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }).orElse(null);
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetail) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(userDetail.getUsername()) && validateToken(token));
    }
}
