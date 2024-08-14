package com.gym.fitcliff.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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

	@Column(nullable = false)
	private String name;

	private LocalDate date;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private List<GroupPaymentDao> payments;

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	private List<CustomerDao> customers;

	public void addCustomer(final CustomerDao customer) {
		customers.add(customer);
		customer.setGroup(this);
	}

	public void addGroupPayment(final GroupPaymentDao groupPayment) {
		payments.add(groupPayment);
		groupPayment.setGroup(this);
	}

}