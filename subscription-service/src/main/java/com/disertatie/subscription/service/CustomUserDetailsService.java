package com.disertatie.subscription.service;

import com.disertatie.subscription.dto.ClientDTO;
import com.disertatie.subscription.dto.RoleDTO;
import com.disertatie.subscription.feign.ClientFeignResource;
import com.disertatie.subscription.feign.RoleFeignResource;
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

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientFeignResource clientFeignResource;

    @Autowired
    private RoleFeignResource roleFeignResource;

    private Logger log = Logger.getLogger(CustomUserDetailsService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws ResponseStatusException {
        log.info("Loading user by username...");

        ClientDTO client = new ClientDTO();

        if (clientFeignResource.getClientByUsername(username) == null) {
            client = clientFeignResource.getClientByEmail(username);
        } else {
            client = clientFeignResource.getClientByUsername(username);
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

    private List<SimpleGrantedAuthority> getAuthority(ClientDTO client) {
        log.info("Fetching authority...");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        RoleDTO role = roleFeignResource.getRoleById(client.getRoleId());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

}
