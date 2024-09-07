package com.gym.fitcliff.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ind_customer_payment")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IndividualPaymentDao {
	/** Primary key */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	@EqualsAndHashCode.Include
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerDao customer;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private String amount;

	private String pendingAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentType paymentType;

	@ManyToOne
	@JoinColumn(name = "membership_duration_id", nullable = false)
	private MemberShipDurationDao membershipDuration;

	// Enum definition
	public enum PaymentType {
		CASH, UPI
	}

}