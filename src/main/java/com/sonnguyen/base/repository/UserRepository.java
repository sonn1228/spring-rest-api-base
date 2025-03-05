package com.sonnguyen.base.repository;

import com.sonnguyen.base.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
//    public User findByUsername(String username);
    public boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
