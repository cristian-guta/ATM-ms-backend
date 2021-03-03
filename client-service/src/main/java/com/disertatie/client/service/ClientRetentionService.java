package com.disertatie.client.service;

import com.disertatie.client.dto.ClientRetentionDTO;
import com.disertatie.client.model.ClientRetention;
import com.disertatie.client.repository.ClientRetentionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientRetentionService {

    private ClientRetentionRepository clientRetentionRepository;

    @Autowired
    public ClientRetentionService(ClientRetentionRepository clientRetentionRepository) throws IOException {
        this.clientRetentionRepository = clientRetentionRepository;
    }



    public Page<ClientRetention> getAllRetentionData(int page, int size){

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ClientRetention> pageResult = clientRetentionRepository.findAll(pageRequest);

        List<ClientRetentionDTO> retentionData = pageResult
                .stream()
                .map(ClientRetentionDTO::new)
                .collect(Collectors.toList());
        return new PageImpl(retentionData, pageRequest, pageResult.getTotalElements());
    }
}
