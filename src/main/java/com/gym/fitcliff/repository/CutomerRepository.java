package com.gym.fitcliff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.fitcliff.entity.Customer;

public interface CutomerRepository extends JpaRepository<Customer, Long> {


}