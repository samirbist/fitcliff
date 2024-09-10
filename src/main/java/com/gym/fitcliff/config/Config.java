package com.gym.fitcliff.config;

import com.gym.fitcliff.model.CustomLogoutSuccessHandler;
import com.gym.fitcliff.service.security.JwtAuthenticationEntryPoint;
import com.gym.fitcliff.service.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class Config {

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

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
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions for REST API
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // Set the logout URL
                        .logoutSuccessHandler(customLogoutSuccessHandler) // Use CustomLogoutSuccessHandler
                        .invalidateHttpSession(true) // Invalidate the session on logout
                        .deleteCookies("JSESSIONID")) // Optionally delete session cookies
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class); // Add custom filter

        return http.build();
    }

    // CORS configuration
    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of("*"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
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
