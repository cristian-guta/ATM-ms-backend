package com.disertatie.account.controller;

import com.disertatie.account.dto.OperationDTO;
import com.disertatie.account.service.OperationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/operations")
@AllArgsConstructor
public class OperationController {
    private OperationService operationService;

    @GetMapping("/{page}/{size}")
    public Page<OperationDTO> getAll(@PathVariable(value = "page") int page,
                                     @PathVariable(value = "size") int size,
                                     Principal principal) {
        return operationService.getAllOperations(page, size, principal);
    }
}
