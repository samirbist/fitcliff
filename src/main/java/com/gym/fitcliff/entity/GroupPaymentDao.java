package com.gym.fitcliff.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "group_payment")
public class GroupPaymentDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupDao group;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String amount;

    private String pendingAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    // Enum definition
    public enum PaymentType {
        CASH, UPI
    }

}