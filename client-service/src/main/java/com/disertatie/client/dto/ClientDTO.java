package com.disertatie.client.dto;

import com.disertatie.client.model.AuthProvider;
import com.disertatie.client.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@Builder
public class ClientDTO {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String cnp;
    private String address;
    private String email;
    private String password;
    private int subscriptionId;
    private Boolean status;
    private int roleId;
    private int imageModelId;
    private String telephoneNumber;
    private AuthProvider authProvider;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.username = client.getUsername();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.cnp = client.getCnp();
        this.address = client.getAddress();
        this.email = client.getEmail();
        this.password = client.getPassword();
        this.status = client.getStatus();
        this.roleId = client.getRoleId();
        this.subscriptionId = client.getSubscriptionId();
        this.imageModelId = client.getImageModelId();
        this.authProvider = client.getAuthProvider();
        this.telephoneNumber = client.getTelephoneNumber();
    }

    public ClientDTO() {

    }

    public static ClientDTO getDto(Client client){
        return ClientDTO.builder()
                .id(client.getId())
                .username(client.getUsername())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .cnp(client.getCnp())
                .address(client.getAddress())
                .email(client.getEmail())
                .password(client.getPassword())
                .status(client.getStatus())
                .roleId(client.getRoleId())
                .subscriptionId(client.getSubscriptionId())
                .imageModelId(client.getImageModelId())
                .authProvider(client.getAuthProvider())
                .telephoneNumber(client.getTelephoneNumber())
                .build();
    }
}
