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
                                .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN") 
                                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/products/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                                // USER-only routes
                                .requestMatchers(HttpMethod.POST, "/orders").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/orders/{id}/payment").hasAnyRole("USER", "ADMIN")

                                // Search route (accessible by USER and ADMIN)
                                .requestMatchers(HttpMethod.GET, "/products/search").hasAnyRole("USER", "ADMIN")

                                // Report routes (ADMIN-only)
                                .requestMatchers(HttpMethod.GET, "/reports/top-customers").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/reports/average-ticket").hasRole("ADMIN") 
                                .requestMatchers(HttpMethod.GET, "/reports/monthly-revenue").hasRole("ADMIN")
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
