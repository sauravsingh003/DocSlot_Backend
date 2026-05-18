package com.app.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.filters.JWTRequestFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

	@Autowired
	private JWTRequestFilter filter;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.cors()
			.and()
			.csrf()
			.disable()

			.exceptionHandling()
			.authenticationEntryPoint((request, response, ex) -> {
				response.sendError(
						HttpServletResponse.SC_UNAUTHORIZED,
						ex.getMessage()
				);
			})

			.and()

			.authorizeRequests()

			// Swagger
			.antMatchers(
					"/v3/api-docs/**",
					"/swagger-ui/**",
					"/swagger-resources/**",
					"/webjars/**",
					"/swagger-ui.html"
			).permitAll()

			// Public APIs
			.antMatchers("/", "/login", "/register").permitAll()

			.antMatchers("/login").permitAll()

			.antMatchers("/patient/registerPatient").permitAll()

			.antMatchers("/patient/getAllDoctors").permitAll()

			.antMatchers(
					"/patient/getDoctorsBySpecialization/**"
			).permitAll()

			.antMatchers("/admin/getAllSpecialization")
			.permitAll()

			// Role-based APIs
			.antMatchers("/admin/**").hasRole("ADMIN")

			.antMatchers("/patient/**").hasRole("PATIENT")

			.antMatchers("/doctor/**").hasRole("DOCTOR")

			.antMatchers("/receptionist/**")
			.hasRole("RECEPTIONIST")

			.antMatchers("/doctor/uploadPrescription/**")
			.hasRole("DOCTOR")

			.antMatchers("/patient/download/**")
			.hasRole("PATIENT")

			.antMatchers(HttpMethod.OPTIONS)
			.permitAll()

			.anyRequest()
			.authenticated()

			.and()

			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()

			.addFilterBefore(
					filter,
					UsernamePasswordAuthenticationFilter.class
			);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticatonMgr(
			AuthenticationConfiguration config
	) throws Exception {

		return config.getAuthenticationManager();
	}
}