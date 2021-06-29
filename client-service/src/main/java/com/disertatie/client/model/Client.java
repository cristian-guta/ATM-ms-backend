package com.disertatie.client.model;

import com.disertatie.client.dto.ClientDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "client", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "cnp", "email"})})
@AllArgsConstructor
@Builder
//@Audited
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
    @NotAudited
    private String password;

    private Boolean status = false;

    private int subscriptionId;

    private String telephoneNumber = "";

    private int roleId;

    private boolean hasUpdated = false;

    private int imageModelId = 0;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    public Client() {
    }

    public static Client getEntity(ClientDTO clientDTO) {
        return Client.builder()
                .firstName(clientDTO.getFirstName())
                .lastName(clientDTO.getLastName())
                .email(clientDTO.getEmail())
                .username(clientDTO.getUsername())
                .password(clientDTO.getPassword())
                .roleId(clientDTO.getRoleId())
                .telephoneNumber(clientDTO.getTelephoneNumber())
                .build();
    }

}
