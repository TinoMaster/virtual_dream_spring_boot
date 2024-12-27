package com.tinomaster.virtualdream.virtualDream.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theme")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Theme {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String primary_color;

	@Column(nullable = false)
	private String secondary_color;

	@Column(nullable = false)
	private String background_color;

	@Column(nullable = false)
	private String text_color;
}
