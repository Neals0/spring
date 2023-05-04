package com.sofianev2.demo.security;

import com.sofianev2.demo.model.Utilisateur;
import jdk.jshell.execution.Util;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MonUserDetails implements UserDetails {
    private Utilisateur utilisateur;

    public MonUserDetails (Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> roles = new ArrayList<>();

        return List.of(new SimpleGrantedAuthority(utilisateur.getRole().getNom()));

        // return List.of(new SimpleGrantedAuthority(utilisateur.isAdmin() ? "ROLE_ADMINISTRATEUR" : "ROLE_UTILISATEUR"));

        //        autre versions possibles

//        if (utilisateur.isAdmin()) {
//            roles.add(new SimpleGrantedAuthority("ADMINISTRATEUR"));
//        } else {
//            roles.add(new SimpleGrantedAuthority("UTILISATEUR"));
//        }
//
//        return roles;

//         return utilisateur.isAdmin()
//                 ? List.of(new SimpleGrantedAuthority("ADMINISTRATEUR"))
//                 : List.of(new SimpleGrantedAuthority("UTILISATEUR"));

    }

    @Override
    public String getPassword() {
        return utilisateur.getMotDePasse();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
