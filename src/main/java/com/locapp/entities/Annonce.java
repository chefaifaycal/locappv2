package com.locapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String descriptionLongue;
    private String descriptionCourte;
    private String adresse;
    private String ville;
    private String grandeur;
    private String typeAnnonce;
    private Date dateDisponibilite;
    private double prix;
    private boolean active;
    private Long user;
}
