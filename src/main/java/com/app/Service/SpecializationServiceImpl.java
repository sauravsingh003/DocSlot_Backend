package com.app.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.DTO.SpecializationDTO;
import com.app.Entity.Specialization;
import com.app.Repository.SpecializationRepository;

@Service
@Transactional
public class SpecializationServiceImpl implements SpecializationService {

	@Autowired
	private SpecializationRepository specializationRepository;

	@Override
	public Specialization addSpecialization(SpecializationDTO dto) {
		Specialization specialization = new Specialization();
		specialization.setName(dto.getName().trim());
		byte[] specializationImage = dto.getSpecializationImage();
		specialization.setSpecializationimage(
				specializationImage != null && specializationImage.length > 0 ? specializationImage : null);
		return specializationRepository.save(specialization);
	}

	@Override
	public List<SpecializationDTO> getAllSpecializations() {
		return specializationRepository.findAll().stream().map(specialization -> {
			SpecializationDTO dto = new SpecializationDTO();
			dto.setId(specialization.getId());
			dto.setName(specialization.getName());
			dto.setSpecializationImage(specialization.getSpecializationimage()); // Return as byte array
			return dto;
		}).collect(Collectors.toList());
	}

}
