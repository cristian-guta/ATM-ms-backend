package com.disertatie.client.controller;

import com.disertatie.client.dto.ClientEmotionsDTO;
import com.disertatie.client.service.ClientEmotionsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client-emotions")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class ClientEmotionsController {

    private ClientEmotionsService clientEmotionsService;

    @GetMapping("/{page}/{size}")
    public Page<ClientEmotionsDTO> getAllEmotions(@PathVariable(value = "page") int page,
                                                  @PathVariable(value = "size") int size) {
        return clientEmotionsService.getAllEmotions(page, size);
    }
}
