package com.gabriel.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.User;
import com.gabriel.ecommerce.exception.UserNotFoundException;
import com.gabriel.ecommerce.repository.UserRepository;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public void notifyUser(String userId, String orderId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        emailService.sendEmail(
            user.getEmail(),
            "Order #" + orderId + " - Cancellation Notice",
            message
        );
    }
}