package org.polytech.spring.rest;

import java.net.URI;
import java.time.Instant;

import org.polytech.spring.patient.DocteurNotFoundException;
import org.polytech.spring.patient.PatientException;
import org.polytech.spring.patient.PatientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Politique d'erreurs de l'API, centralisée.
 *
 * L'extension de ResponseEntityExceptionHandler permet de traiter également les
 * exceptions levées par Spring MVC lui-même (corps illisible, méthode non
 * supportée, média non supporté), qui ne passent pas par les @ExceptionHandler
 * d'un contrôleur. Depuis Spring 6, cette classe produit des ProblemDetail par
 * défaut.
 *
 * Format de réponse : RFC 9457, Content-Type application/problem+json.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BASE = "https://api.polytech.fr/errors/";

    @ExceptionHandler(PatientNotFoundException.class)
    public ProblemDetail handleNotFound(PatientNotFoundException e) {
        return problem(HttpStatus.NOT_FOUND, "Patient introuvable", e.getMessage(), "patient-introuvable");
    }

    @ExceptionHandler(DocteurNotFoundException.class)
    public ProblemDetail handleDocteurNotFound(DocteurNotFoundException e) {
        return problem(HttpStatus.NOT_FOUND, "Docteur introuvable", e.getMessage(), "docteur-introuvable");
    }

    @ExceptionHandler(PatientException.class)
    public ProblemDetail handleInvalid(PatientException e) {
        return problem(HttpStatus.BAD_REQUEST, "Patient invalide", e.getMessage(), "patient-invalide");
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, String type) {
        ProblemDetail pb = ProblemDetail.forStatusAndDetail(status, detail);
        pb.setTitle(title);
        pb.setType(URI.create(BASE + type));
        pb.setProperty("timestamp", Instant.now());
        return pb;
    }
}
