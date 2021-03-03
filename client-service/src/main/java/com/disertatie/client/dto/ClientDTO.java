package com.disertatie.client.dto;

import com.disertatie.client.model.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
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
    }

    public ClientDTO() {

    }
}
