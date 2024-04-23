package com.locapp.controller;

import com.locapp.dto.request.SignUpRequest;
import com.locapp.dto.request.SigninRequest;
import com.locapp.dto.response.JwtAuthenticationResponse;
import com.locapp.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
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
}
