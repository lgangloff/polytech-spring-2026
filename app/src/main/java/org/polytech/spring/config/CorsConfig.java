package org.polytech.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Politique CORS unique, définie en un seul endroit.
 *
 * L'alternative — une annotation @CrossOrigin sur chaque contrôleur — disperse
 * la politique dans le code et expose à l'oubli d'un contrôleur.
 *
 * Les origines proviennent d'une propriété externalisée, et non du code. En
 * production, allowedOrigins("*") est à proscrire : cela autoriserait n'importe
 * quel site à appeler l'API depuis le navigateur des utilisateurs.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
