package com.disertatie.client.dto;

import com.disertatie.client.model.ClientEmotion;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClientEmotionsDTO {
    private int id;
    private String emotion;
    private int clientId;

    public ClientEmotionsDTO(ClientEmotion clientEmotion) {
        this.id = clientEmotion.getId();
        this.emotion = clientEmotion.getEmotion();
        this.clientId = clientEmotion.getClient().getId();
    }

    public ClientEmotionsDTO() {

    }
}
