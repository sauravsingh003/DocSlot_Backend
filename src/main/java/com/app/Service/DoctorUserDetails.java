package com.app.Service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.Entity.Doctor;

@SuppressWarnings("serial")
public class DoctorUserDetails implements UserDetails {

    private final Doctor doctor;

    public DoctorUserDetails(Doctor doctor) {

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }

        this.doctor = doctor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.asList(
                new SimpleGrantedAuthority(
                        doctor.getRole().name()
                )
        );
    }

    @Override
    public String getPassword() {
        return doctor.getPassword();
    }

    @Override
    public String getUsername() {
        return doctor.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return doctor.getId();
    }

    public String getName() {
        return doctor.getName();
    }

    public String getRole() {
        return doctor.getRole().name();
    }
}