package com.disertatie.client.model;

import com.disertatie.client.security.AuditRevisionListener;
import lombok.Data;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.*;

@Data
@Entity
@Table(name = "revinfo")
@RevisionEntity(AuditRevisionListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "timestamp", column = @Column(name = "revtstmp")),
        @AttributeOverride(name = "id", column = @Column(name = "rev"))
})
public class RevisionInfo extends DefaultRevisionEntity {

    @Column(name = "user")
    private String user;
}
