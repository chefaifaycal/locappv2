package com.locapp.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.locapp.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
    // Clé de signature JWT récupérée à partir des propriétés
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    // Méthode pour extraire le nom d'utilisateur à partir du token JWT
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Méthode pour générer un token JWT à partir des détails de l'utilisateur
    @Override
    public String generateToken(UserDetails userDetails) {
        // Générer un token avec des réclamations supplémentaires vides
        return generateToken(new HashMap<>(), userDetails);
    }

    // Méthode pour vérifier la validité d'un token JWT par rapport aux détails de l'utilisateur
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        // Vérifier si le nom d'utilisateur extrait du token correspond à celui des détails de l'utilisateur
        // et si le token n'est pas expiré
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Méthode générique pour extraire des réclamations d'un token JWT
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        // Extraire toutes les réclamations du token
        final Claims claims = extractAllClaims(token);
        // Appliquer la fonction de résolution de réclamations aux réclamations extraites
        return claimsResolvers.apply(claims);
    }

    // Méthode pour générer un token JWT avec des réclamations supplémentaires et les détails de l'utilisateur
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Construire le token JWT avec les réclamations supplémentaires, le nom d'utilisateur, la date d'émission
        // et la date d'expiration
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Expiration dans 24 heures
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact(); // Signature avec l'algorithme HS256
    }

    // Méthode pour vérifier si un token JWT est expiré
    private boolean isTokenExpired(String token) {
        // Extraire la date d'expiration du token et comparer avec la date actuelle
        return extractExpiration(token).before(new Date());
    }

    // Méthode pour extraire la date d'expiration d'un token JWT
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Méthode pour extraire toutes les réclamations d'un token JWT
    private Claims extractAllClaims(String token) {
        // Parser le token JWT et extraire les réclamations
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    // Méthode pour obtenir la clé de signature à partir de la clé secrète JWT
    private Key getSigningKey() {
        // Décoder la clé secrète JWT de Base64
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        // Générer une clé HMAC à partir des octets de clé
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
