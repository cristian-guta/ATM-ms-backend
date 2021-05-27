package com.disertatie.client.controller;

import com.disertatie.client.model.ClientRetention;
import com.disertatie.client.service.ClientRetentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/clients-retention")
@PreAuthorize("hasRole('ADMIN')")
public class ClientRetentionController {

    private ClientRetentionService clientRetentionService;

    @Autowired
    public ClientRetentionController(ClientRetentionService clientRetentionService) {
        this.clientRetentionService = clientRetentionService;
    }

    @GetMapping("/{page}/{size}")
    public Page<ClientRetention> getAllRetentionData(@PathVariable(value = "page") int page,
                                                     @PathVariable(value = "size") int size){
        return clientRetentionService.getAllRetentionData(page, size);
    }
}
