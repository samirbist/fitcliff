package com.gym.fitcliff.repository;

import com.gym.fitcliff.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
    List<BlacklistedToken> findByExpiresAtBefore(LocalDateTime now);
}