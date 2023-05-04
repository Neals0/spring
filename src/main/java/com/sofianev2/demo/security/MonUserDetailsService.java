package com.sofianev2.demo.security;

import com.sofianev2.demo.dao.UtilisateurDao;
import com.sofianev2.demo.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MonUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurDao utilisateurDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Utilisateur> optional = utilisateurDao.findByEmail(email);

        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("L'utilisateur n'existe pas");
        }

        return new MonUserDetails(optional.get());
    }

}
