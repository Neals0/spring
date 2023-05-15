package com.sofianev2.demo.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.sofianev2.demo.view.VueUtilisateur;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter

public class Materiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private int numero;

}