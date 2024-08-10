package com.gym.fitcliff.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "customer_phones", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "phone")
    private List<String> phones;

    private String photoMongoId;
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

   
}