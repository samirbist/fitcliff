package com.gym.fitcliff.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "image")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ImageDao {

	/** Primary key */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	@EqualsAndHashCode.Include
	private Long id;

	@OneToOne(mappedBy = "image")
	private CustomerDao customer;

	private String fileName;
	private LocalDate createdOn;
	private String mongoId;

	@Column(nullable = false)
	private boolean isActive;

	// Constructor to ensure the default value is set
	public ImageDao() {
		this.isActive = true;
	}
}
