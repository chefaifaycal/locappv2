package com.locapp.controller;

import com.locapp.dto.request.SignUpRequest;
import com.locapp.dto.request.SigninRequest;
import com.locapp.dto.response.JwtAuthenticationResponse;
import com.locapp.entities.User;
import com.locapp.repository.UserRepository;
import com.locapp.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
    @PostMapping("/password/reset")
    public void resetPassword(@RequestParam String email,
                              @RequestParam String newPassword) {
        authenticationService.resetPassword(email, newPassword);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with -> username or email : " + username)
        );

        // Créez une classe DTO si nécessaire ou retournez simplement un Map
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());

        return ResponseEntity.ok(response);
    }
}
