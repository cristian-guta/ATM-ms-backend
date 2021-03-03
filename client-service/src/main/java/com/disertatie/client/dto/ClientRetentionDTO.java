package com.disertatie.client.dto;

import com.disertatie.client.model.ClientRetention;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ClientRetentionDTO {

    private int id;
    private int clientId;

    public ClientRetentionDTO(){}

    public ClientRetentionDTO(ClientRetention clientRetention) {
        this.clientId = clientRetention.getClientId();
        this.id = clientRetention.getId();
    }
}
