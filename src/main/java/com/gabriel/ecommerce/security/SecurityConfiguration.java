package com.gabriel.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                            // Public routes (no authentication required)
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                                // ADMIN-only routes
                                .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN") // Create product
                                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN") // Update product
                                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN") // Delete product

                                // USER-only routes
                                .requestMatchers(HttpMethod.POST, "/orders").hasRole("USER") // Create order
                                .requestMatchers(HttpMethod.GET, "/products").hasRole("USER") // View products
                                .requestMatchers(HttpMethod.POST, "/orders/{id}/payment").hasRole("USER") // Pay for order

                                // Search route (accessible by USER and ADMIN)
                                .requestMatchers(HttpMethod.GET, "/products/search").hasAnyRole("USER", "ADMIN") // Search products

                                // Report routes (ADMIN-only)
                                .requestMatchers(HttpMethod.GET, "/reports/top-customers").hasRole("ADMIN") // Top 5 users by purchases
                                .requestMatchers(HttpMethod.GET, "/reports/average-ticket").hasRole("ADMIN") // Average ticket per user
                                .requestMatchers(HttpMethod.GET, "/reports/monthly-revenue").hasRole("ADMIN") // Monthly revenue
                            .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
