package com.tinomaster.virtualdream.virtualDream.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "machine")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Machine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "business_id", referencedColumnName = "id")
	private Business business;

	@Column(nullable = false)
	private boolean active;
}
