package com.disertatie.client.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "client_aud")
@Data
@Accessors(chain = true)
@IdClass(ClientAuditId.class)
public class ClientAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Id
    private int rev;

    private int revtype;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "cnp")
    private String cnp;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private Boolean status = false;

    @Column(name = "subscription_id")
    private int subscriptionId;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "has_updated")
    private boolean hasUpdated = false;

    @Column(name = "image_model_id")
    private int imageModelId = 0;

    @Column(name = "auth_provider")
    private String authProvider;

    @Column(name = "user")
    private String user;
}
