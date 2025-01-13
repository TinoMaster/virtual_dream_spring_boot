package com.tinomaster.virtualdream.virtualdream.entities;

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
@Table(name = "consumable_cost")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumableCost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "consumable_id", referencedColumnName = "id")
	private Consumable consumable;

	@Column(nullable = false)
	private Float quantity;
}
