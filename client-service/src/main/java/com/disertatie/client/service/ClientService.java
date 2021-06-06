package com.disertatie.client.service;

import com.disertatie.client.dto.ClientDTO;
import com.disertatie.client.dto.ResultDTO;
import com.disertatie.client.model.Client;
import com.disertatie.client.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDTO updateClient(int id, ClientDTO updatedClient) {
        Client client = clientRepository.getById(id)
                .setAddress(updatedClient.getAddress())
                .setFirstName(updatedClient.getFirstName())
                .setLastName(updatedClient.getLastName())
                .setEmail(updatedClient.getEmail())
                .setUsername(updatedClient.getUsername())
                .setSubscriptionId(updatedClient.getSubscriptionId());
        return new ClientDTO(clientRepository.save(client));
    }

    public Page<ClientDTO> getAll(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Client> pageResult = clientRepository.findAll(pageRequest);

        List<ClientDTO> clients = pageResult
                .stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(clients, pageRequest, pageResult.getTotalElements());
    }

    public ClientDTO getCurrentClient(Principal principal) {

        Client client = new Client();
        if (clientRepository.findByUsername(principal.getName()) == null) {
            client = clientRepository.findClientByEmail(principal.getName());
        } else {
            client = clientRepository.findByUsername(principal.getName());
        }

        ClientDTO clientDTO = new ClientDTO()
                .setId(client.getId())
                .setAddress(client.getAddress())
                .setCnp(client.getCnp())
                .setEmail(client.getEmail())
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setPassword(client.getPassword())
                .setStatus(client.getStatus())
                .setUsername(client.getUsername())
                .setSubscriptionId(client.getSubscriptionId());
        return clientDTO;
    }

    public ResultDTO deactivateClient(Integer id) {
        return changeClientStatus(id, true);
    }

    public ResultDTO activateClient(Integer id) {
        return changeClientStatus(id, false);
    }

    private ResultDTO changeClientStatus(Integer id, Boolean status) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            client.get().setStatus(status);
            clientRepository.save(client.get());
            return new ResultDTO().setStatus(true).setMessage("Successfully changed user status!");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }

    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }

    public ClientDTO findByUsername(String username) {
        if (clientRepository.findByUsername(username) != null) {
            return new ClientDTO(clientRepository.findByUsername(username));
        } else {
            return new ClientDTO(clientRepository.findClientByEmail(username));
        }
    }

    public ClientDTO findByEmail(String email) {
        return new ClientDTO(clientRepository.findClientByEmail(email));
    }
}
