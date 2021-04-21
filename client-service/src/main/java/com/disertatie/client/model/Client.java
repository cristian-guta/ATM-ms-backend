package com.disertatie.client.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "client", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "cnp", "email"})})
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //    @NotNull
    private String username = "";

    private String firstName = "";

    private String lastName = "";

    private String cnp = "";

    private String address = "";

    private String email = "";

    //    @JsonIgnore
    private String password;

    @NotNull
    private Boolean status = false;

    private int subscriptionId;

    private int roleId;

    private boolean hasUpdated = false;

    private int imageModelId = 0;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    public Client() {
    }

}
