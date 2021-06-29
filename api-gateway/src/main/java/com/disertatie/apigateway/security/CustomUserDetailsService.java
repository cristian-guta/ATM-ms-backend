package com.disertatie.apigateway.security;

import com.disertatie.apigateway.dto.ClientDTO;
import com.disertatie.apigateway.model.AuthProvider;
import com.disertatie.apigateway.model.Client;
import com.disertatie.apigateway.model.Role;
import com.disertatie.apigateway.repository.ClientRepository;
import com.disertatie.apigateway.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private Logger log = Logger.getLogger(CustomUserDetailsService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws ResponseStatusException {
        log.info("Loading user by username...");

        Client client = new Client();

        if (clientRepository.findByUsername(username) == null) {
            client = clientRepository.findClientByEmail(username);
        } else {
            client = clientRepository.findByUsername(username);
        }

        if (client == null) {
            log.info("User not found...");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        } else if (client.getStatus()) {
            log.info("Invalid credentials...");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials!");
        }
        log.info("Loading...");
        if (client.getUsername() != null) {
            return new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(),
                    getAuthority(client));
        } else {
            return new org.springframework.security.core.userdetails.User(client.getEmail(), client.getPassword(),
                    getAuthority(client));
        }
    }

    private List<SimpleGrantedAuthority> getAuthority(Client client) {
        log.info("Fetching authority...");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Role role = client.getRole();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

    public Client save(ClientDTO user) {
        log.info("Saving new client account...");
        if (clientRepository.findByUsername(user.getUsername()) != null || clientRepository.findClientByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User already exists!");
        }
        Client newUser = new Client()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setPassword(bcryptEncoder.encode(user.getPassword()))
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setCnp(user.getCnp())
                .setAddress(user.getAddress())
                .setAuthProvider(AuthProvider.local)
                .setImageModelId(user.getImageModelId())
                .setTelephoneNumber(user.getTelephoneNumber())
                .setSubscriptionId(0);
        Role role = roleRepository.findByName("USER");
        newUser.setRole(role);
        log.info("New client saved...");
        return clientRepository.save(newUser);
    }

    public JwtResponse handleLogin(JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            log.info("User disabled...");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            log.info("Invalid credentials...");
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
