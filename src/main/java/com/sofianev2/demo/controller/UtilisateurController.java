package com.sofianev2.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sofianev2.demo.dao.UtilisateurDao;
import com.sofianev2.demo.model.Role;
import com.sofianev2.demo.model.Utilisateur;
import com.sofianev2.demo.security.JwtUtils;
import com.sofianev2.demo.services.FichierService;
import com.sofianev2.demo.view.VueUtilisateur;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
@CrossOrigin
public class UtilisateurController {

    @Autowired
    private UtilisateurDao utilisateurDao;


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    FichierService fichierService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/utilisateurs")
    @JsonView(VueUtilisateur.class)
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurDao.findAll();
    }

    @GetMapping("/utilisateur/{id}")
    @JsonView(VueUtilisateur.class)
    public ResponseEntity<Utilisateur> getUtilisateurSofiane(@PathVariable int id) {

//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // pour trigger ERROR_500

        Optional<Utilisateur> optional = utilisateurDao.findById(id);

        if (optional.isPresent()) {
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/utilisateur-par-pays/{nomPays}")
    @JsonView(VueUtilisateur.class)
    public List<Utilisateur> getUtilisateur(@PathVariable String nomPays) {


        return utilisateurDao.trouveUtilisateurSelonPays(nomPays);
    }

    @GetMapping("/profil")
    public ResponseEntity<Utilisateur> getProfil(@RequestHeader("Authorization") String bearer) {
        String jwt = bearer.substring(7);
        Claims donnees = jwtUtils.getData(jwt);
        Optional<Utilisateur> utilisateur = utilisateurDao.findByEmail(donnees.getSubject());

        if (utilisateur.isPresent()) {
            return new ResponseEntity<>(utilisateur.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/image-profil/{idUtilisateur}")
    public ResponseEntity<byte[]> getImageProfil(@PathVariable int idUtilisateur) {

        Optional<Utilisateur> optional = utilisateurDao.findById(idUtilisateur);

        if (optional.isPresent()) {
            String nomImage = optional.get().getNomImageProfil();

            try {
                byte[] image = fichierService.getImageByName(nomImage);

                HttpHeaders enTete = new HttpHeaders();
                String mineType = Files.probeContentType(new File(nomImage).toPath());
                enTete.setContentType(MediaType.valueOf(mineType));

                return new ResponseEntity<>(image, enTete, HttpStatus.OK);

            } catch (FileNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (IOException e) {
                System.out.println("Le test du mimetype a échoué");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/admin/utilisateur")
    public ResponseEntity<Utilisateur> ajoutUtilisateur(
            @RequestPart("utilisateur") Utilisateur nouvelUtilisateur,
            @Nullable @RequestParam("fichier") MultipartFile fichier
    ) {

        //si l'utilisateur fournit possède un id
        if (nouvelUtilisateur.getId() != null) {

            Optional<Utilisateur> optional = utilisateurDao.findById(nouvelUtilisateur.getId());

            //si c'est un update
            if (optional.isPresent()) {

                Utilisateur userToUpdate = optional.get();
                userToUpdate.setNom(nouvelUtilisateur.getNom());
                userToUpdate.setPrenom(nouvelUtilisateur.getPrenom());
                userToUpdate.setPays(nouvelUtilisateur.getPays());

                utilisateurDao.save(userToUpdate);

                if (fichier != null) {
                    try {
                        fichierService.transfertVersDossierUpload(fichier, "image_profil");
                    } catch (IOException e) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                return new ResponseEntity<>(nouvelUtilisateur, HttpStatus.OK);
            }

            //si il y a eu une tentative d'insertion d'un utilisateur avec un id qui n'existait pas
            return new ResponseEntity<>(nouvelUtilisateur, HttpStatus.BAD_REQUEST);

        }

        Role role = new Role();
        role.setId(1); // par défaut lors d'un ajout d'user on associe le role n°1
        nouvelUtilisateur.setRole(role);

        String passwordHache = passwordEncoder.encode("root");
        nouvelUtilisateur.setMotDePasse(passwordHache);


        if (fichier != null) {
            try {

                String nomImage = UUID.randomUUID() + "_" + fichier.getOriginalFilename();
                nouvelUtilisateur.setNomImageProfil(nomImage);

                fichierService.transfertVersDossierUpload(fichier, nomImage);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        utilisateurDao.save(nouvelUtilisateur);

        return new ResponseEntity<>(nouvelUtilisateur, HttpStatus.CREATED);

    }


    @DeleteMapping("/admin/utilisateur/{id}")
    @JsonView(VueUtilisateur.class)
    public ResponseEntity<Utilisateur> supprimerUtilisateur(@PathVariable int id) {

        Optional<Utilisateur> utilisateurAsupprimer = utilisateurDao.findById(id);


        if (utilisateurAsupprimer.isPresent()) {
            utilisateurDao.deleteById(id);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
