package com.tinomaster.virtualdream.virtualdream.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualdream.dtos.ThemeDto;
import com.tinomaster.virtualdream.virtualdream.entities.Theme;
import com.tinomaster.virtualdream.virtualdream.repositories.ThemeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ThemeService {

	private final ThemeRepository themeRepository;
	private final ModelMapper mapper;

	public List<Theme> getAllThemes() {
		return themeRepository.findAll();
	}

	public Theme getThemeById(Long id) {
		return themeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("No se ha encontrado el tema con el id: " + id));
	}

	public Theme saveTheme(ThemeDto themeDto) {
		return themeRepository.save(mapper.map(themeDto, Theme.class));
	}
}
