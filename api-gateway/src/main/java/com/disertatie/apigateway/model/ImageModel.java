package com.disertatie.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="image_model")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ImageModel {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    public ImageModel(String originalFilename, String contentType, byte[] bytes) {
        this.name = originalFilename;
        this.type = contentType;
        this.pic = bytes;
    }
}
