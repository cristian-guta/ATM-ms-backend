package com.disertatie.apigateway.controller;

import com.disertatie.apigateway.dto.ClientDTO;
import com.disertatie.apigateway.model.Client;
import com.disertatie.apigateway.repository.ClientRepository;
import com.disertatie.apigateway.security.CustomUserDetailsService;
import com.disertatie.apigateway.security.JwtRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ClientDTO client) throws SQLException {
        return ResponseEntity.ok(userDetailsService.save(client));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(userDetailsService.handleLogin(authenticationRequest));
    }

    @GetMapping("/currentUser")
    public Client getUserLoggedIn() {
        return getCurrentUserLoggedIn();
    }

    public Client getCurrentUserLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return clientRepository.findByUsername(auth.getName());
    }

}
