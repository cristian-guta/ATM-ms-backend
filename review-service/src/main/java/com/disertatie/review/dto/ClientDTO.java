package com.disertatie.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ClientDTO {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String cnp;
    private String address;
    private String email;
    private String password;
    private Boolean status;
    private int roleId;

    public ClientDTO(String username, String firstName, String lastName, String cnp, String address, String email, int roleId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnp = cnp;
        this.address = address;
        this.email = email;
        this.roleId = roleId;
    }
}
