package com.disertatie.apigateway.model;

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
    private String username;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String telephoneNumber = "";

    private String cnp;

    private String address;

    @NotNull
    private String email;

    //    @JsonIgnore
    private String password;

    @NotNull
    private Boolean status = false;

    private int subscriptionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean hasUpdated = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private int imageModelId;

    public Client() {
    }

}
