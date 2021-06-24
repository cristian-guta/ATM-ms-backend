package com.disertatie.client.controller;

import com.disertatie.client.dto.ClientDTO;
import com.disertatie.client.dto.ResultDTO;
import com.disertatie.client.model.Client;
import com.disertatie.client.repository.ClientRepository;
import com.disertatie.client.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ClientService clientService;
    private ClientRepository clientRepository;

    public ClientController(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public List<ClientDTO> findAll() {
        return clientService.findAll();
    }

    @PutMapping("/update")
    public ClientDTO updateClient(@RequestParam("id") int theId, @RequestBody ClientDTO updatedClient) {
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

    @GetMapping("/username")
    public ClientDTO getByUsername(@RequestParam("username") String username) {
        return clientService.findByUsername(username);
    }

    @GetMapping("/email")
    public ClientDTO getByEmail(@RequestParam("email") String email) {
        return clientService.findByEmail(email);
    }

    @GetMapping("/current")
    public ClientDTO getCurrentClient(Principal principal) {
        return clientService.getCurrentClient(principal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/deactivate")
    public ResultDTO deactivateClient(@RequestParam("id") int theId) {
        return clientService.deactivateClient(theId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/activate")
    public ResultDTO activateClient(@RequestParam("id") Integer id) {
        return clientService.activateClient(id);
    }

    @PostMapping("/create")
    public ResultDTO createClient(@RequestBody ClientDTO clientDTO) {
        return clientService.create(clientDTO);
    }
}
