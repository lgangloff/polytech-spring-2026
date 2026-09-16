package org.polytech.spring.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Une entité est une classe annotée @Entity, dotée d'un @Id et d'un
 * constructeur sans argument accessible.
 *
 * Par défaut, la table porte le nom de la classe et chaque attribut correspond
 * à une colonne de même nom, le camelCase étant converti en snake_case :
 * codePostal devient code_postal.
 */
@Entity
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    private String rue;

    @Column(name = "code_postal", length = 5)
    private String codePostal;

    @Column(nullable = false)
    private String ville;

    public Adresse() {
    }

    public Adresse(String numero, String rue, String codePostal, String ville) {
        this.numero = numero;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "%s %s, %s %s".formatted(numero, rue, codePostal, ville);
    }
}
