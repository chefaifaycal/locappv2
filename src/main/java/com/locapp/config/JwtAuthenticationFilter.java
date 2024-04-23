package com.locapp.config;

import java.io.IOException;

import com.locapp.service.JwtService;
import com.locapp.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService; // Service pour la gestion des JWT
    private final UserService userService; // Service pour la gestion des utilisateurs

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Récupération de l'en-tête d'autorisation
        final String jwt;
        final String userEmail;

        // Vérification si l'en-tête d'autorisation est vide ou ne commence pas par "Bearer "
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response); // Passer au filtre suivant dans la chaîne
            return;
        }

        jwt = authHeader.substring(7); // Extraction du JWT de l'en-tête
        userEmail = jwtService.extractUserName(jwt); // Extraction du nom d'utilisateur à partir du JWT

        // Vérification si le nom d'utilisateur extrait n'est pas vide
        // et si aucune authentification n'est déjà en cours dans le contexte de sécurité
        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Récupération des détails de l'utilisateur à partir du service utilisateur
            UserDetails userDetails = userService.userDetailsService()
                    .loadUserByUsername(userEmail);

            // Vérification si le token JWT est valide pour les détails de l'utilisateur
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Création d'un contexte de sécurité vide
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                // Création d'une instance d'authentification à partir des détails de l'utilisateur
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Ajout des détails de l'authentification Web
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Attribution de l'authentification au contexte de sécurité
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context); // Définition du contexte de sécurité
            }
        }

        filterChain.doFilter(request, response); // Passer au filtre suivant dans la chaîne
    }
}
