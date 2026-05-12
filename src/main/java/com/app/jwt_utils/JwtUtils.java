package com.app.jwt_utils;

import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.app.Service.CustomerUserDetails;
import com.app.Service.DoctorUserDetails;
import com.app.Service.PatientUserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${SECRET_KEY}")
    private String jwtSecret;

    @Value("${EXP_TIMEOUT}")
    private int jwtExpirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generate JWT Token
    public String generateJwtToken(Authentication authentication) {

        UserDetails userPrincipal =
                (UserDetails) authentication.getPrincipal();

        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(authority -> authority.getAuthority())
                .orElse("UNKNOWN");

        Long userId = null;

        // Extract ID based on user type
        if (userPrincipal instanceof DoctorUserDetails) {

            userId = ((DoctorUserDetails) userPrincipal).getId();

        } else if (userPrincipal instanceof PatientUserDetails) {

            userId = ((PatientUserDetails) userPrincipal).getId();

        } else if (userPrincipal instanceof CustomerUserDetails) {

            userId = ((CustomerUserDetails) userPrincipal).getId();
        }

        System.out.println("Generating JWT for: " + userPrincipal.getUsername());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("role", role)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                (new Date()).getTime() + jwtExpirationMs
                        )
                )
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Extract username
    public String getUserNameFromJwtToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extract role
    public String getRoleFromJwtToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // Extract ID
    public Long getIdFromJwtToken(String token) {

        Integer id = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Integer.class);

        return id != null ? id.longValue() : null;
    }

    // Validate JWT
    public boolean validateJwtToken(String jwtToken) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);

            return true;

        } catch (Exception e) {

            System.out.println("Invalid JWT token: " + e.getMessage());
        }

        return false;
    }
}