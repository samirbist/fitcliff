package com.gym.fitcliff.entity;

import jakarta.persistence.*;
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
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(nullable = false)
	private String name;

	@Temporal(TemporalType.DATE)
	private Date date;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private List<GroupPayment> payments;

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	private List<Customer> customers;

	public void addCustomer(final Customer customer) {
		customers.add(customer);
		customer.setGroup(this);
	}

	public void addGroupPayment(final GroupPayment groupPayment) {
		payments.add(groupPayment);
		groupPayment.setGroup(this);
	}

}