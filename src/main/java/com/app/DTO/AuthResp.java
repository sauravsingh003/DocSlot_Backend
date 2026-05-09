package com.app.DTO;

import org.springframework.security.core.Authentication;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthResp {

    private String message;
    private String jwt;
    private Authentication authenticatedDetails;

    public AuthResp() {

    }

    public AuthResp(String message, String jwt, Authentication authenticatedDetails) {
        this.message = message;
        this.jwt = jwt;
        this.authenticatedDetails = authenticatedDetails;
    }
}