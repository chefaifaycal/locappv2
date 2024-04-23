package com.locapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String nom;
    private String prenom;
    private String NumTel;
    private String email;
    private String adresse;
    private String password;
}

