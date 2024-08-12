package com.gym.fitcliff.entity;

import jakarta.persistence.*;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ind_customer_payment")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IndividualPayment {
	/** Primary key */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	@EqualsAndHashCode.Include
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date date;

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