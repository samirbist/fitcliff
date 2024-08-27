package com.gym.fitcliff.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.fitcliff.entity.UserDao;

public interface UserRepository extends JpaRepository<UserDao, Long>{

	Optional<UserDao> findByUserName(String name);
}
