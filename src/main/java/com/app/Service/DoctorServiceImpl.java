package com.app.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.DTO.DoctorDTO;
import com.app.DTO.DoctorDTOResponse;
import com.app.DTO.DoctorDtoImage;
import com.app.Entity.Doctor;
import com.app.Entity.Role;
import com.app.Entity.Specialization;
import com.app.Repository.DoctorRepository;
import com.app.Repository.SpecializationRepository;
import com.app.Repository.UserRepository;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SpecializationRepository specializationRepository;

	public String addDoctor(DoctorDTO doctorDTO) {

		Doctor doctor = new Doctor();

		doctor.setEmail(doctorDTO.getEmail());
		doctor.setPassword(doctorDTO.getPassword());
		doctor.setName(doctorDTO.getName());
		doctor.setPhone(doctorDTO.getPhone());
		doctor.setDegree(doctorDTO.getDegree());
		doctor.setAmount(doctorDTO.getAmount());
		doctor.setRole(Role.ROLE_DOCTOR);

		// Set specialization
		Specialization specialization =
				specializationRepository.findById(
						doctorDTO.getSpecializationId()
				).orElseThrow(() ->
						new RuntimeException("Specialization not found")
				);

		doctor.setSpecialization(specialization);

		/*
		 * TEMPORARY FIX:
		 * Disable doctor image storage to isolate PostgreSQL OID issue.
		 * Image persistence will be redesigned later.
		 */
		doctor.setDoctorimage(null);

		doctorRepository.save(doctor);

		return "Doctor and User added successfully!";
	}

	public List<DoctorDTOResponse> getAllDoctors() {
		return doctorRepository.getAllDoctorsLightweight();
	}

	// Get doctor by ID
	@Override
	public DoctorDTOResponse getDoctorById(Long id) {

		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(() ->
						new RuntimeException(
								"Doctor with ID " + id + " not found"
						)
				);

		return new DoctorDTOResponse(
				doctor.getId(),
				doctor.getName(),
				doctor.getEmail(),
				doctor.getPassword(),
				doctor.getPhone(),
				doctor.getDegree(),
				doctor.getAmount(),
				doctor.getSpecialization() != null
						? doctor.getSpecialization().getName()
						: null
		);
	}

	public List<DoctorDtoImage> getDoctorsBySpecializationId(
			Long specializationId
	) {

		List<Doctor> doctors =
				doctorRepository.findBySpecializationId(
						specializationId
				);

		return doctors.stream()
				.map(doctor ->
						new DoctorDtoImage(
								doctor.getId(),
								doctor.getName(),
								doctor.getPhone(),
								doctor.getDegree(),
								doctor.getAmount(),
								doctor.getSpecialization() != null
										? doctor.getSpecialization().getName()
										: null,
								null // image disabled temporarily
						)
				)
				.collect(Collectors.toList());
	}

	public String updateDoctor(Long id, DoctorDTO doctorDTO) {

		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(() ->
						new RuntimeException(
								"Doctor not found with ID: " + id
						)
				);

		doctor.setName(doctorDTO.getName());
		doctor.setEmail(doctorDTO.getEmail());
		doctor.setPassword(doctorDTO.getPassword());
		doctor.setPhone(doctorDTO.getPhone());
		doctor.setDegree(doctorDTO.getDegree());
		doctor.setRole(Role.ROLE_DOCTOR);
		doctor.setAmount(doctorDTO.getAmount());

		Specialization specialization =
				specializationRepository.findById(
						doctorDTO.getSpecializationId()
				).orElseThrow(() ->
						new RuntimeException("Specialization not found")
				);

		doctor.setSpecialization(specialization);

		// Keep image disabled temporarily
		doctor.setDoctorimage(null);

		doctorRepository.save(doctor);

		return "Doctor updated successfully!";
	}
}