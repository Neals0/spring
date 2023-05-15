package com.sofianev2.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.sofianev2.demo.view.VueEntreprise;
import com.sofianev2.demo.view.VueUtilisateur;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
//@Table(name="utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({VueUtilisateur.class, VueEntreprise.class})
    private Integer id;

    //@Column(length = 80, nullable = false)
    @JsonView({VueUtilisateur.class, VueEntreprise.class})
    private String nom;

    //@Column(length = 50, nullable = true)
    @JsonView({VueUtilisateur.class, VueEntreprise.class})
    private String prenom;

    @JsonView({VueUtilisateur.class, VueEntreprise.class})
    private String email;

    @JsonView(VueUtilisateur.class)
    private String nomImageProfil;

    private String motDePasse;

    @ManyToOne
    @JsonView(VueUtilisateur.class)
    private Pays pays;

    @JsonView({VueUtilisateur.class, VueEntreprise.class})
    @ManyToMany
    @JoinTable(name = "role_utilisateur", inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    //@JsonIgnore
    @ManyToOne
    @JsonView(VueUtilisateur.class)
    private Entreprise entreprise;

    @ManyToMany
    @JoinTable(
            name = "recherche_emploi_utilisateur",
            inverseJoinColumns = @JoinColumn(name = "emploi_id")
    )

    @JsonView(VueUtilisateur.class)
    private Set<Emploi> emploisRecherche = new HashSet<>();

    @CreationTimestamp
    @JsonView(VueUtilisateur.class)
    private LocalDate createdAt;

    @UpdateTimestamp
    @JsonView(VueUtilisateur.class)
    private LocalDateTime updatedAt;

}
