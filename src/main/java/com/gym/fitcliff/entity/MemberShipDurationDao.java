package com.gym.fitcliff.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "duration")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MemberShipDurationDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(nullable = false, unique=true)
	private String duration;
	
	
}
