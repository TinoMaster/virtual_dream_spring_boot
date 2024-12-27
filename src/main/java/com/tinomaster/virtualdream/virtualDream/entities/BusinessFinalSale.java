package com.tinomaster.virtualdream.virtualDream.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "business_final_sale")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessFinalSale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "business_id", referencedColumnName = "id")
	private Business business;
	
	@Column(nullable = false)
	private Float total;
	
	@Column(nullable = false)
	private Float paid;
	
	@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "business_final_sale_id")
	private List<Debt> debts;
	
	@Column(nullable = true)
	private String note;
	
	@OneToMany
	private List<Employee> workers;
	
	@Column(nullable = false, updatable = false, unique = true)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
