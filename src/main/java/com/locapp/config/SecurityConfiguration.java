package com.locapp.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.locapp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Filtre JWT pour l'authentification
    private final UserService userService; // Service pour la gestion des utilisateurs

    // Configuration de la chaîne de filtres de sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Désactivation de la protection CSRF
                .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**")
                        .permitAll().anyRequest().authenticated()) // Configuration des autorisations des requêtes HTTP
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS)) // Gestion de la session sans état
                .authenticationProvider(authenticationProvider()) // Configuration du fournisseur d'authentification
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Ajout du filtre JWT avant le filtre d'authentification par nom d'utilisateur/mot de passe
        return http.build(); // Construction de la chaîne de filtres de sécurité
    }

    // Bean pour l'encodeur de mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilisation de BCryptPasswordEncoder pour l'encodage des mots de passe
    }

    // Bean pour le fournisseur d'authentification
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Création d'un fournisseur d'authentification DAO
        authProvider.setUserDetailsService(userService.userDetailsService()); // Configuration du service utilisateur comme source de détails de l'utilisateur
        authProvider.setPasswordEncoder(passwordEncoder()); // Configuration de l'encodeur de mots de passe
        return authProvider; // Retour du fournisseur d'authentification configuré
    }

    // Bean pour le gestionnaire d'authentification
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager(); // Récupération du gestionnaire d'authentification à partir de la configuration
    }
}
