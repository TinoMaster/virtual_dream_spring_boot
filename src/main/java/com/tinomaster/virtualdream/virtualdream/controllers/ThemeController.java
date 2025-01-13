package com.tinomaster.virtualdream.virtualdream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualdream.dtos.ThemeDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.Theme;
import com.tinomaster.virtualdream.virtualdream.services.ThemeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ThemeController {

	private final ThemeService themeService;
	private final ModelMapper mapper;

	private ThemeDto themeToThemeDto(Theme theme) {
		return mapper.map(theme, ThemeDto.class);
	}

	@GetMapping("/private/theme")
	public ResponseEntity<ResponseBody<List<ThemeDto>>> getAllThemes() {
		var themeList = StreamSupport.stream(themeService.getAllThemes().spliterator(), false).toList();
		List<ThemeDto> themes = themeList.stream().map(this::themeToThemeDto).collect(Collectors.toList());
		return ResponseType.ok("successfullyRequest", themes);
	}

	@GetMapping("/superadmin/theme/{id}")
	public ResponseEntity<ResponseBody<ThemeDto>> getThemeById(@PathVariable Long id) {
		ThemeDto theme = this.themeToThemeDto(themeService.getThemeById(id));
		return ResponseType.ok("successfullyRequest", theme);
	}

	@PostMapping("/superadmin/theme")
	public ResponseEntity<ResponseBody<ThemeDto>> saveTheme(@RequestBody ThemeDto themeDto) {
		return ResponseType.ok("successfullySaved", this.themeToThemeDto(themeService.saveTheme(themeDto)));
	}
}
