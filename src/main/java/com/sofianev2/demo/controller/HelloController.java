package com.sofianev2.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    // Quelle route est liée à cette méthode ? > GetMapping
    public String hello() {
        return "Le serveur marche mais y'a rien ici !";
    }
}
