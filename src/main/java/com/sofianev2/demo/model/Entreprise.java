package com.sofianev2.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.sofianev2.demo.view.VueEntreprise;
import com.sofianev2.demo.view.VueUtilisateur;
import jdk.jshell.execution.Util;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({VueUtilisateur.class, VueEntreprise.class})
    private Integer id;

    @JsonView({VueUtilisateur.class, VueEntreprise.class})
    private String nom;

    @OneToMany(mappedBy = "entreprise")
    @JsonView(VueEntreprise.class)
    private Set<Utilisateur> listeEmploye = new HashSet<>();

}
