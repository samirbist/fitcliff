package com.gym.fitcliff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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

		  http
          .csrf(csrf -> csrf.disable())  // Disable CSRF protection (for stateless REST APIs)
          .authorizeRequests(authorizeRequests -> authorizeRequests
              .requestMatchers("/admin/**").hasRole("ADMIN")  // Restrict /admin/** URLs to ADMIN role
              .requestMatchers("/auth/login").permitAll()     // Allow anyone to access /auth/login
              .anyRequest().authenticated()                   // All other URLs require authentication
          )
          .exceptionHandling(ex -> ex.authenticationEntryPoint(point))  // Custom authentication entry point
          .sessionManagement(session -> session
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Use stateless sessions for REST API
          )
          .logout(logout -> logout
              .logoutUrl("/auth/logout")  // Set the logout URL
              .logoutSuccessHandler(customLogoutSuccessHandler())  // Define custom behavior on logout success
              .invalidateHttpSession(true)  // Invalidate the session on logout
              .deleteCookies("JSESSIONID")  // Delete specific cookies on logout (optional)
          )
          .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);  // Add custom filter

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
}