package com.gym.fitcliff.scheduler;

import com.gym.fitcliff.entity.BlacklistedToken;
import com.gym.fitcliff.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenCleanupService {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

     @Scheduled(cron = "0 0 0 * * ?") // Daily at midnight
	// @Scheduled(cron = "0 * * * * *") //runs every minute
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<BlacklistedToken> expiredTokens = blacklistedTokenRepository.findByExpiresAtBefore(now);
        blacklistedTokenRepository.deleteAll(expiredTokens);
    }
}
