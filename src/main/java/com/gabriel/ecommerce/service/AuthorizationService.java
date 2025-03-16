package com.gabriel.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.User;
import com.gabriel.ecommerce.entity.dto.RegisterDTO;
import com.gabriel.ecommerce.enums.Role;
import com.gabriel.ecommerce.repository.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public boolean createNewUser(RegisterDTO data) {
        
        if (repository.findByUsername(data.username()) != null) { return false;}

        if (Role.ADMIN.equals(data.role())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();

            if (!Role.ADMIN.equals(authenticatedUser.getRole())) {
                throw new AccessDeniedException("Only admins can create other admin users.");
            }
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.username(), encryptedPassword, data.role(), data.email());

        repository.save(user);

        return true;

    }
}
