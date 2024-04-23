package com.locapp.service.impl;

import com.locapp.repository.UserRepository;
import com.locapp.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository; // Repository pour accéder aux données des utilisateurs

    @Override
    public UserDetailsService userDetailsService() {
        // Retourne une instance de UserDetailsService qui charge les détails de l'utilisateur par son nom d'utilisateur
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                // Recherche de l'utilisateur dans la base de données par son email
                return userRepository.findByEmail(username)
                        // Retourne les détails de l'utilisateur s'il est trouvé, sinon lance une exception
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
