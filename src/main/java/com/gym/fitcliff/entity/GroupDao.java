package com.gym.fitcliff.entity;

import java.time.LocalDate;
import java.util.List;

import com.gym.fitcliff.entity.CustomerDao.MembershipDuration;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_group")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupDao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(name = "grpname", nullable = false, unique = true)
	private String name;

	private LocalDate date;

	@Column(name = "membership_amount", nullable = false)
	private String membershipAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "membership_duration", nullable = false)
	private MembershipDuration membershipDuration;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GroupPaymentDao> payments;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CustomerDao> customers;

	public enum MembershipDuration {
		ONE_MONTH, THREE_MONTHS, SIX_MONTHS, ONE_YEAR
	}

	public void addCustomer(final CustomerDao customer) {
		customers.add(customer);
		customer.setGroup(this);
	}

	public void addGroupPayment(final GroupPaymentDao groupPayment) {
		payments.add(groupPayment);
		groupPayment.setGroup(this);
	}

}