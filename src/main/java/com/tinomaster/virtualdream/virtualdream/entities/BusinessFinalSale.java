package com.tinomaster.virtualdream.virtualdream.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "business_final_sale")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessFinalSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    private Business business;

    @Column(nullable = false)
    private Float total;

    @Column(nullable = false)
    private Float paid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Debt> debts;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ServiceSale> servicesSale;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Card> cards;

    //TODO: Ver aqui motivo por el cual no me deja salvar un business final sale si existe otro por las maquinas
    // problema que seguramente se expande a los demas campos seteados de la misma manera
//    @ManyToMany // <--- Change to ManyToMany
//    @JoinTable( // Optional but good practice: Define join table details
//            name = "business_final_sale_machines",
//            joinColumns = @JoinColumn(name = "business_final_sale_id"),
//            inverseJoinColumns = @JoinColumn(name = "machine_id")
//    )
//    @ToString.Exclude
//    private List<Machine> machines;

    @OneToMany
    @ToString.Exclude
    private List<Machine> machines;

    @Column(nullable = true)
    private String note;

    @OneToMany
    @ToString.Exclude
    private List<Employee> workers;

    @ManyToOne
    private User doneBy;

    @Column(nullable = false)
    private Float fund;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
