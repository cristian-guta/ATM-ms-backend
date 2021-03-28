package com.disertatie.client.controller;

import com.disertatie.client.dto.ClientDTO;
import com.disertatie.client.dto.ResultDTO;
import com.disertatie.client.model.Client;
import com.disertatie.client.repository.ClientRepository;
import com.disertatie.client.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/clients")
@CrossOrigin("*")
public class ClientController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ClientService clientService;
    private ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/findAll")
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public Client updateClient(@PathVariable("id") int theId, @RequestBody ClientDTO updatedClient) {
        return clientService.updateClient(theId, updatedClient);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{page}/{size}")
    public Page<ClientDTO> getAll(@PathVariable(value = "page") int page,
                                  @PathVariable(value = "size") int size) {
        return clientService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public Client getById(@PathVariable int id) {
        return clientRepository.getById(id);
    }

    @GetMapping("/username/{username}")
    public Client getByUsername(@PathVariable String username) {
        if (clientRepository.findByUsername(username) != null) {
            return clientRepository.findByUsername(username);
        } else {
            return clientRepository.findClientByEmail(username);
        }

    }

    @GetMapping("/email/{email}")
    public Client getByEmail(@PathVariable String email) {
        return clientRepository.findClientByEmail(email);
    }

    @GetMapping("/current")
    public ClientDTO getCurrentClient(Principal principal) {
        return clientService.getCurrentClient(principal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResultDTO deleteClient(@PathVariable("id") int theId) {
        return clientService.deactivateClient(theId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/activate/{id}")
    public ResultDTO activateClient(@PathVariable(value = "id") Integer id) {
        return clientService.activateClient(id);
    }
}
