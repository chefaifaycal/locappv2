package com.locapp.service.impl;

import com.locapp.dto.request.SignUpRequest;
import com.locapp.dto.request.SigninRequest;
import com.locapp.dto.response.JwtAuthenticationResponse;
import com.locapp.entities.Role;
import com.locapp.entities.User;
import com.locapp.repository.UserRepository;
import com.locapp.service.AuthenticationService;
import com.locapp.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository; // Repository pour accéder aux données des utilisateurs
    private final PasswordEncoder passwordEncoder; // Encodeur de mot de passe pour sécuriser les mots de passe
    private final JwtService jwtService; // Service pour la gestion des JWT
    private final AuthenticationManager authenticationManager; // Gestionnaire d'authentification de Spring Security

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        // Création d'un nouvel utilisateur à partir des données de la demande
        var user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .numtel(request.getNumTel())
                .email(request.getEmail())
                .adresse(request.getAdresse())
                .password(passwordEncoder.encode(request.getPassword())) // Encodage du mot de passe
                .role(Role.USER) // Attribution du rôle utilisateur par défaut
                .build();

        // Sauvegarde du nouvel utilisateur dans la base de données
        userRepository.save(user);

        // Génération d'un token JWT pour l'utilisateur nouvellement inscrit
        var jwt = jwtService.generateToken(user);

        // Création et retour d'une réponse contenant le token JWT
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        // Authentification de l'utilisateur avec le gestionnaire d'authentification
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Recherche de l'utilisateur dans la base de données par son email
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        // Génération d'un token JWT pour l'utilisateur authentifié
        var jwt = jwtService.generateToken(user);

        // Création et retour d'une réponse contenant le token JWT
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        // Recherche de l'utilisateur dans la base de données par son email
        User user = userRepository.findByEmail(email)
                // Retourne l'utilisateur s'il est trouvé, sinon lance une exception
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Modification du mot de passe de l'utilisateur avec le nouveau mot de passe encodé
        user.setPassword(passwordEncoder.encode(newPassword));

        // Sauvegarde de l'utilisateur mis à jour dans la base de données
        userRepository.save(user);
    }
}
