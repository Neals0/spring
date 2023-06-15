package com.sofianev2.demo.dao;

import com.sofianev2.demo.model.ImageDto;
import com.sofianev2.demo.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurDao extends JpaRepository<Utilisateur, Integer> {

    Utilisateur findByPrenom(String prenom);
    @Query("FROM Utilisateur U JOIN FETCH U.roles WHERE U.email = :email")
    Optional<Utilisateur> findByEmail(@Param("email") String email);

    @Query("FROM Utilisateur U JOIN U.pays P WHERE P.nom = :pays")
    List<Utilisateur> trouveUtilisateurSelonPays(@Param("pays") String pays);


}
