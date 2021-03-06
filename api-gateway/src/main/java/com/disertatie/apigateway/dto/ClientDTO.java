package com.disertatie.apigateway.dto;

import com.disertatie.apigateway.model.AuthProvider;
import com.disertatie.apigateway.model.Client;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientDTO {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String cnp;
    private String address;
    private String email;
    private String password;
    private String telephoneNumber;
    private int subscriptionId;
    private AuthProvider authProvider;
    private Boolean status;
    private int imageModelId;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.username = client.getUsername();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.cnp = client.getCnp();
        this.address = client.getAddress();
        this.email = client.getEmail();
        this.authProvider = client.getAuthProvider();
        this.status = client.getStatus();
        this.telephoneNumber = client.getTelephoneNumber();
        this.subscriptionId = client.getSubscriptionId();
        this.imageModelId = client.getImageModelId();
    }

    public ClientDTO(String username, String firstName, String telephoneNumber, String lastName, String cnp, String address, String email, int subscriptionId, int imageModelId, AuthProvider authProvider) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnp = cnp;
        this.address = address;
        this.email = email;
        this.subscriptionId = subscriptionId;
        this.authProvider = authProvider;
        this.imageModelId = imageModelId;
        this.telephoneNumber = telephoneNumber;
    }

    public ClientDTO() {
    }
}
