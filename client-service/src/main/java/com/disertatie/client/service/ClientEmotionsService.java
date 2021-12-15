package com.disertatie.client.service;

import com.disertatie.client.dto.ClientEmotionsDTO;
import com.disertatie.client.model.ClientEmotion;
import com.disertatie.client.repository.ClientEmotionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientEmotionsService {

    private ClientEmotionRepository clientEmotionRepository;

    public Page<ClientEmotionsDTO> getAllEmotions(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ClientEmotion> pageResult = clientEmotionRepository.findAll(pageRequest);

        List<ClientEmotionsDTO> retentionData = pageResult
                .stream()
                .map(ClientEmotionsDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(retentionData, pageRequest, pageResult.getTotalElements());
    }
}
