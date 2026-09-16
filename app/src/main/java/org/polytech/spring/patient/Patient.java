package org.polytech.spring.patient;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nom;

    private String prenom;

    private String email;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    /**
     * Un patient habite à une seule adresse, et cette adresse n'a pas
     * d'existence propre : cascade ALL et orphanRemoval. Supprimer le patient
     * supprime son adresse ; la détacher de l'objet la supprime également.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adresse")
    private Adresse adresse;

    /**
     * Côté propriétaire de la relation bidirectionnelle avec Docteur : c'est
     * lui qui porte la clé étrangère id_docteur.
     *
     * Toutes les relations sont déclarées LAZY, y compris celle-ci dont la
     * valeur par défaut serait EAGER. Le chargement est demandé explicitement,
     * au moyen d'un JOIN FETCH.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docteur")
    private Docteur medecinTraitant;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Docteur getMedecinTraitant() {
        return medecinTraitant;
    }

    public void setMedecinTraitant(Docteur medecinTraitant) {
        this.medecinTraitant = medecinTraitant;
    }

    @Override
    public String toString() {
        return "Patient[id=%s, prenom=%s, nom=%s, email=%s]".formatted(id, prenom, nom, email);
    }
}
