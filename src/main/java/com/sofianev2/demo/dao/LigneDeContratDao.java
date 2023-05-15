package com.sofianev2.demo.dao;

import com.sofianev2.demo.model.LigneDeContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneDeContratDao extends JpaRepository<LigneDeContrat, LigneDeContrat.IdLigneDeContrat> {

}
