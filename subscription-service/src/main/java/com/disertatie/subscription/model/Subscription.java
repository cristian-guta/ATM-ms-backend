package com.disertatie.subscription.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "subscription", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotAudited
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subscriptions_benefits", joinColumns = {
            @JoinColumn(name = "subscription_id")}, inverseJoinColumns = {
            @JoinColumn(name = "benefit_id")
    })
    private List<Benefit> benefits;
}
