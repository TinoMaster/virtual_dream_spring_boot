package com.tinomaster.virtualdream.virtualDream.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(nullable = false)
	private Integer quantity;

	@ManyToOne
	@JoinColumn(name = "service_id", referencedColumnName = "id")
	private ServiceEntity service;

	@ManyToOne
	@JoinColumn(name = "business_final_sale_id", referencedColumnName = "id")
	private BusinessFinalSale businessFinalSale;

	@ManyToOne
	@JoinColumn(name = "business_id", referencedColumnName = "id")
	private Business business;

	@Column(nullable = false, updatable = false, unique = true)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
