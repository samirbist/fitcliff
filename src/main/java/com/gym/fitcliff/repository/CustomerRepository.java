package com.gym.fitcliff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.fitcliff.entity.CustomerDao;

public interface CustomerRepository extends JpaRepository<CustomerDao, Long> {


}