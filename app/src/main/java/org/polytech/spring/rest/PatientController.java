package org.polytech.spring.rest;

import java.net.URI;
import java.util.List;

import org.polytech.spring.dto.PatientCreationDto;
import org.polytech.spring.dto.PatientDetailDto;
import org.polytech.spring.dto.PatientDto;
import org.polytech.spring.patient.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    /** GET /patients, et GET /patients?nom=love pour la recherche. */
    @GetMapping
    public List<PatientDto> getAll(@RequestParam(required = false) String nom) {
        return nom == null ? service.findAll() : service.findByNom(nom);
    }

    @GetMapping("/{id:\\d+}")
    public PatientDetailDto getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<PatientDto> create(@RequestBody PatientCreationDto body) {
        PatientDto created = service.create(body);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    /** Affecte un médecin traitant : deux lectures et une écriture dans une même transaction. */
    @PutMapping("/{id:\\d+}/medecin/{docteurId:\\d+}")
    public PatientDto affecterMedecin(@PathVariable Long id, @PathVariable Long docteurId) {
        return service.affecterMedecin(id, docteurId);
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
