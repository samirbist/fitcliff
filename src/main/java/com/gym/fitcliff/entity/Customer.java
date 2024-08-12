package com.gym.fitcliff.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "customer")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    @Column
    private String photoMongoId;
    
    @Column
    private String docMongoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private boolean isActive;

    @Temporal(TemporalType.DATE)
    private Date regDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date joinDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthdate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false)
    private String membershipAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipDuration membershipDuration;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<IndividualPayment> payments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipType membershipType;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private DocumentImage documentImage;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    // Enum definitions
    public enum Gender {
        MALE, FEMALE
    }

    public enum MembershipDuration {
        ONE_MONTH, THREE_MONTHS, SIX_MONTHS, ONE_YEAR
    }

    public enum MembershipType {
        INDIVIDUAL, GROUP
    }
    
    
    public void addPhone(final Phone phone) {
        phones.add(phone);
        phone.setCustomer(this);
      }
    
    public void addIndividualPayment(final IndividualPayment individualPayment) {
    	payments.add(individualPayment);
    	individualPayment.setCustomer(this);
      }

   
}