package com.disertatie.apigateway.controller;

import com.disertatie.apigateway.dto.ClientDTO;
import com.disertatie.apigateway.model.Client;
import com.disertatie.apigateway.repository.ClientRepository;
import com.disertatie.apigateway.security.CustomUserDetailsService;
import com.disertatie.apigateway.security.JwtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController
{

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody ClientDTO client)
	{
		return ResponseEntity.ok(userDetailsService.save(client));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception
	{
		return ResponseEntity.ok(userDetailsService.handleLogin(authenticationRequest));
	}

	@GetMapping("/currentUser")
	public Client getUserLoggedIn()
	{
		return getCurrentUserLoggedIn();
	}

	public Client getCurrentUserLoggedIn()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return clientRepository.findByUsername(auth.getName());
	}

}
