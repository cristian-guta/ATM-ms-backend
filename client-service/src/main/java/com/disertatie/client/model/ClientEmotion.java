package com.disertatie.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "client_emotions")
@AllArgsConstructor
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ClientEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String emotion;

//    private double anger;
//    private double contempt;
//    private double disgust;
//    private double fear;
//    private double happiness;
//    private double neutral;
//    private double sadness;
//    private double surprise;

    @ManyToOne
    private Client client;

}
