package com.disertatie.subscription.dto;

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
    private int imageModelId;
    private String telephoneNumber;
    private String hasUpdated;

    public ClientDTO() {

    }
}
