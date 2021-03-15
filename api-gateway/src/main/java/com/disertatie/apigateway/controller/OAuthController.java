package com.disertatie.apigateway.controller;

import com.disertatie.apigateway.dto.TokenDTO;
import com.disertatie.apigateway.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OAuthController {

    @Autowired
    public OAuthService oAuthService;


    @PostMapping("/google")
    public ResponseEntity<TokenDTO> google(@RequestBody TokenDTO tokenDto) throws IOException {
        return oAuthService.googleLogin(tokenDto);
    }
}
