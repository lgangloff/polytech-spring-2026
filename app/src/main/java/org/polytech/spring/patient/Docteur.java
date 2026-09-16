package org.polytech.spring.patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Docteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nom;

    private String prenom;

    /**
     * Le numéro RPPS identifie un professionnel de santé : 11 chiffres, unique
     * au niveau national. D'où les trois contraintes portées par @Column.
     */
    @Column(nullable = false, unique = true, length = 11)
    private String rpps;

    @Column(name = "date_diplome")
    private LocalDate dateDiplome;

    @Enumerated(EnumType.STRING)
    private Specialite specialite;

    /**
     * Plusieurs docteurs peuvent partager le même cabinet : la relation est un
     * @ManyToOne, matérialisé par une colonne id_adresse. Aucune cascade :
     * supprimer un docteur ne doit pas supprimer le cabinet.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adresse")
    private Adresse adresse;

    /**
     * Côté inverse de la relation bidirectionnelle : mappedBy désigne
     * l'attribut propriétaire dans Patient. Sans lui, JPA créerait une table de
     * jointure docteur_patients en plus de la colonne id_docteur.
     *
     * Aucune cascade non plus : la patientèle a une existence propre.
     */
    @OneToMany(mappedBy = "medecinTraitant")
    private List<Patient> patients = new ArrayList<>();

    public Docteur() {
    }

    public Docteur(String prenom, String nom, String rpps, Specialite specialite) {
        this.prenom = prenom;
        this.nom = nom;
        this.rpps = rpps;
        this.specialite = specialite;
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

    public String getRpps() {
        return rpps;
    }

    public void setRpps(String rpps) {
        this.rpps = rpps;
    }

    public LocalDate getDateDiplome() {
        return dateDiplome;
    }

    public void setDateDiplome(LocalDate dateDiplome) {
        this.dateDiplome = dateDiplome;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
