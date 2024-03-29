package com.disertatie.client.service;

import com.disertatie.client.dto.ClientDTO;
import com.disertatie.client.dto.ResultDTO;
import com.disertatie.client.model.AuthProvider;
import com.disertatie.client.model.Client;
import com.disertatie.client.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ClientService
{

	private ClientRepository clientRepository;
	private PasswordEncoder bcryptEncoder;

	public ClientDTO updateClient(int id, ClientDTO updatedClient)
	{
		Client client = clientRepository.getById(id)
				.setAddress(updatedClient.getAddress())
				.setFirstName(updatedClient.getFirstName())
				.setLastName(updatedClient.getLastName())
				.setEmail(updatedClient.getEmail())
				.setUsername(updatedClient.getUsername())
				.setTelephoneNumber(updatedClient.getTelephoneNumber())
				.setSubscriptionId(updatedClient.getSubscriptionId());
		return ClientDTO.getDto(clientRepository.save(client));
	}

	public Page<ClientDTO> getAll(int page, int size)
	{

		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Client> pageResult = clientRepository.findAll(pageRequest);

		List<ClientDTO> clients = pageResult
				.stream()
				.map(ClientDTO::getDto)
				.collect(Collectors.toList());
		return new PageImpl<>(clients, pageRequest, pageResult.getTotalElements());
	}

	public ClientDTO getCurrentClient(Principal principal)
	{

		Client client;
		if (Optional.ofNullable(clientRepository.findClientByEmail(principal.getName())).isPresent())
		{
			client = clientRepository.findClientByEmail(principal.getName());
		}
		else
		{
			client = clientRepository.findByUsername(principal.getName());
		}

		return ClientDTO.getDto(client);
	}

	public ResultDTO deactivateClient(Integer id)
	{
		return changeClientStatus(id, true);
	}

	public ResultDTO activateClient(Integer id)
	{
		return changeClientStatus(id, false);
	}

	private ResultDTO changeClientStatus(Integer id, Boolean status)
	{
		Optional<Client> client = clientRepository.findById(id);
		if (client.isPresent())
		{
			client.get().setStatus(status);
			clientRepository.save(client.get());
			return new ResultDTO().setStatus(true).setMessage("Successfully changed user status!");
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
		}
	}

	public List<ClientDTO> findAll()
	{
		return clientRepository.findAll().stream()
				.map(ClientDTO::getDto)
				.collect(Collectors.toList());
	}

	public ClientDTO findByUsername(String username)
	{
		if (clientRepository.findByUsername(username) != null)
		{
			return ClientDTO.getDto(clientRepository.findByUsername(username));
		}
		else
		{
			return ClientDTO.getDto(clientRepository.findClientByEmail(username));
		}
	}

	public ClientDTO findByEmail(String email)
	{
		return ClientDTO.getDto(clientRepository.findClientByEmail(email));
	}

	public ResultDTO create(ClientDTO clientDTO)
	{
		Client client = Client.getEntity(clientDTO);
		client.setPassword(bcryptEncoder.encode(client.getPassword()));
		client.setAuthProvider(AuthProvider.local);
		client.setStatus(false);
		clientRepository.save(client);
		return new ResultDTO().setMessage("New user created!").setStatus(true);
	}
}
