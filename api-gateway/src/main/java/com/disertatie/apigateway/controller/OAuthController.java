package com.disertatie.apigateway.controller;

import com.disertatie.apigateway.dto.TokenDTO;
import com.disertatie.apigateway.model.AuthProvider;
import com.disertatie.apigateway.model.Client;
import com.disertatie.apigateway.model.Role;
import com.disertatie.apigateway.repository.ClientRepository;
import com.disertatie.apigateway.repository.RoleRepository;
import com.disertatie.apigateway.security.CustomUserDetailsService;
import com.disertatie.apigateway.security.JwtTokenUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OAuthController {

    @Value("${spring:\n" +
            "  security:\n" +
            "    oauth2:\n" +
            "      client:\n" +
            "        registration:\n" +
            "          google:\n" +
            "            client-id}")
    String googleClientId;

    @Value("${secretPsw}")
    String secretPsw;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/google")
    public ResponseEntity<TokenDTO> google(@RequestBody TokenDTO tokenDto) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                        .setAudience(Collections.singletonList(googleClientId));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
        Client client = new Client();
        if (clientRepository.findClientByEmail(payload.getEmail()) == null) {
            client = saveClient(payload.getEmail(), AuthProvider.google);

        } else
            client = clientRepository.findClientByEmail(payload.getEmail());
        TokenDTO tokenRes = login(client);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<TokenDTO> facebook(@RequestBody TokenDTO tokenDto) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        final String[] fields = {"email", "picture"};
        User user = facebook.fetchObject("me", User.class, fields);
        Client client = new Client();
        if (clientRepository.findClientByEmail(user.getEmail()) != null)
            client = clientRepository.findClientByEmail(user.getEmail());
        else
            client = saveClient(user.getEmail(), AuthProvider.facebook);
        TokenDTO tokenRes = login(client);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }


    private TokenDTO login(Client client) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(client.getEmail(), secretPsw)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(client.getEmail());
        String jwt = jwtTokenUtil.generateToken(userDetails);
        TokenDTO tokenDto = new TokenDTO();
        tokenDto.setValue(jwt);
        return tokenDto;
    }

    private Client saveClient(String email, AuthProvider authProvider) {
        Client client = new Client();
        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(secretPsw));
        client.setAuthProvider(authProvider);
        Role userRole = roleRepository.findByName("USER");
        client.setRole(userRole);
        client.setAuthProvider(AuthProvider.google);
        return clientRepository.save(client);
    }
}
