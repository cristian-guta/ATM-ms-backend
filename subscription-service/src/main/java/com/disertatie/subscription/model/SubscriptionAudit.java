package com.disertatie.subscription.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "subscription_aud")
@Data
@Accessors(chain = true)
@IdClass(SubscriptionAuditId.class)
public class SubscriptionAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Id
    private int rev;

    private int revtype;
    private String name;
    private Double price;

    @Column(name = "subscription_network")
    private String subscriptionNetwork;
    private String user;
}
