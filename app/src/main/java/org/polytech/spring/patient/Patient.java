package org.polytech.spring.patient;

import java.time.LocalDate;

/**
 * Objet métier utilisé dans l'ensemble du cours.
 * Ce n'est pas encore une entité JPA : voir le chapitre « Java Persistence API ».
 */
public class Patient {

    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private LocalDate dateNaissance;

    public Patient() {
    }

    public Patient(String prenom, String nom) {
        this(prenom, nom, null);
    }

    public Patient(String prenom, String nom, String email) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String toString() {
        return "Patient[id=%s, prenom=%s, nom=%s, email=%s]".formatted(id, prenom, nom, email);
    }
}
