package org.polytech.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Plus aucune méthode @Bean : @ComponentScan demande à Spring de parcourir les
 * packages à la recherche des classes annotées (@Component et ses stéréotypes).
 *
 * Sans argument, le scan part du package de cette classe et couvre ses
 * sous-packages — ici « org.polytech.spring ». Il peut aussi être explicite :
 * @ComponentScan("org.polytech.spring").
 */
@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class AppConfig {
}
