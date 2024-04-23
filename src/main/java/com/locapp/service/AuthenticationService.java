package com.locapp.service;


import com.locapp.dto.request.SignUpRequest;
import com.locapp.dto.request.SigninRequest;
import com.locapp.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    void resetPassword(String email, String newPassword);
}
