package com.gym.fitcliff.model;

import com.gym.fitcliff.entity.BlacklistedToken;
import com.gym.fitcliff.repository.BlacklistedTokenRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	private BlacklistedTokenRepository blacklistedTokenRepository;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// Extract JWT token from request header
		String token = extractToken(request);

		if (token != null) {
			// Blacklist the token with an expiration date
			BlacklistedToken blacklistedToken = new BlacklistedToken();
			blacklistedToken.setToken(token);
			blacklistedToken.setBlacklistedAt(LocalDateTime.now());
			blacklistedToken.setExpiresAt(LocalDateTime.now().plusDays(7)); // Adjust as needed
			blacklistedTokenRepository.save(blacklistedToken);
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("Logout successful!");
		response.getWriter().flush();
	}

	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7); // Remove "Bearer " prefix
		}
		return null;
	}


}
