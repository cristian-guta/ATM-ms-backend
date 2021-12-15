package com.disertatie.client.service;

import com.disertatie.client.model.Client;
import com.disertatie.client.model.Role;
import com.disertatie.client.repository.ClientRepository;
import com.disertatie.client.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService
{

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws ResponseStatusException
	{
		log.info("Loading user by username...");

		Client client;

		if (clientRepository.findByUsername(username) == null)
		{
			client = clientRepository.findClientByEmail(username);
		}
		else
		{
			client = clientRepository.findByUsername(username);
		}
		if (client == null)
		{
			log.info("User not found...");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
		}
		else if (client.getStatus().equals(Boolean.TRUE))
		{
			log.info("Invalid credentials...");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials!");
		}
		log.info("Loading...");
		if (client.getUsername() != null)
		{
			return new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(),
					getAuthority(client));
		}
		else
		{
			return new org.springframework.security.core.userdetails.User(client.getEmail(), client.getPassword(),
					getAuthority(client));
		}
	}

	private List<SimpleGrantedAuthority> getAuthority(Client client)
	{
		log.info("Fetching authority...");
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		Role role = roleRepository.findById(client.getRoleId());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		return authorities;
	}

}
