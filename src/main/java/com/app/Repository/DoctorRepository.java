package com.app.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.DTO.DoctorDTOResponse;
import com.app.Entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	List<Doctor> findBySpecializationId(Long specializationId);

	Optional<Doctor> findByEmail(String email);

	// Lightweight query for public doctor listing
	@Query("SELECT new com.app.DTO.DoctorDTOResponse(" +
	       "d.id, " +
	       "d.name, " +
	       "d.email, " +
	       "d.password, " +
	       "d.phone, " +
	       "d.degree, " +
	       "d.amount, " +
	       "s.name" +
	       ") " +
	       "FROM Doctor d " +
	       "LEFT JOIN d.specialization s")
	List<DoctorDTOResponse> getAllDoctorsLightweight();
}