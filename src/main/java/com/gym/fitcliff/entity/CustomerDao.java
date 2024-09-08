package com.gym.fitcliff.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "customer")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CustomerDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = true, unique = false, length = 255)
    private String email;

    @Column(nullable = false, length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private boolean isActive;
    
    @Column(nullable = false)
    private LocalDate  regDate;

    @Column(nullable = false)
    private LocalDate  lastDate;
    
    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(nullable = true)
    private LocalDate  birthdate;

    @Column(nullable = false, length = 1000)
    private String address;

    @Column(nullable = false)
    private String membershipAmount;

 
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndividualPaymentDao> payments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageDao image;

    // Enum definitions
    public enum Gender {
        MALE, FEMALE
    }


    
    public void addIndividualPayment(final IndividualPaymentDao individualPayment) {
    	payments.add(individualPayment);
    	individualPayment.setCustomer(this);
      }

   
}