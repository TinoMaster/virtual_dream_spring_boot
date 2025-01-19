package com.tinomaster.virtualdream.virtualdream.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @ManyToOne
    @JoinColumn(name = "business_final_sale_id", referencedColumnName = "id")
    private BusinessFinalSale businessFinalSale;

    @Column(nullable = false)
    private Float amount;
}
