package com.gabriel.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.ecommerce.entity.User;
import com.gabriel.ecommerce.entity.dto.AuthenticationDTO;
import com.gabriel.ecommerce.entity.dto.LoginResponseDTO;
import com.gabriel.ecommerce.entity.dto.RegisterDTO;
import com.gabriel.ecommerce.service.AuthorizationService;
import com.gabriel.ecommerce.service.TokenService;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity <Object> login (@RequestBody @Validated AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
                
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity <Object> register (@RequestBody @Validated RegisterDTO data) {

        boolean created = authorizationService.createNewUser(data);
        if(!created){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
