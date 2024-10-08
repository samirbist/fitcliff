package com.gym.fitcliff.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gym.fitcliff.service.security.JwtAuthenticationEntryPoint;
import com.gym.fitcliff.service.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class Config {

	@Autowired
	private JwtAuthenticationEntryPoint point;

	@Autowired
	private JwtAuthenticationFilter filter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Disable CSRF protection (for stateless REST APIs)
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with custom config
				.authorizeRequests(authorizeRequests -> authorizeRequests
						.requestMatchers("/static/**", "/resources/**", "/public/**", "/webjars/**").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN") // Restrict /admin/** URLs to ADMIN role
						.requestMatchers("/auth/login").permitAll() // Allow anyone to access /auth/login
						.anyRequest().authenticated() // All other URLs require authentication
				)

				.exceptionHandling(ex -> ex.authenticationEntryPoint(point)) // Custom authentication entry point
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use
																												// stateless
																												// sessions
																												// for
																												// REST
																												// API
				).logout(logout -> logout.logoutUrl("/auth/logout") // Set the logout URL
						.logoutSuccessHandler(customLogoutSuccessHandler()) // Define custom behavior on logout success
						.invalidateHttpSession(true) // Invalidate the session on logout
						.deleteCookies("JSESSIONID") // Delete specific cookies on logout (optional)
				).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class); // Add custom filter

		return http.build();
	}

	// Define a custom logout success handler
	@Bean
	public LogoutSuccessHandler customLogoutSuccessHandler() {
		return (request, response, authentication) -> {
			// Custom behavior on logout success
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("Logout successful!");
			response.getWriter().flush();
		};
	}

	// CORS configuration
	@Bean
	public CorsConfiguration corsConfiguration() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(List.of("*"));
		// corsConfig.setAllowedOrigins(List.of("http://localhost:3000")); // Allow your
		// frontend origin
		corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow specific HTTP methods
		corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Allow specific headers
		corsConfig.setAllowCredentials(true); // Allow cookies or credentials

		return corsConfig;
	}

	// CORS configuration source
	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration()); // Apply CORS settings to all endpoints
		return source;
	}
}
