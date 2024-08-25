package com.gym.fitcliff.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.fitcliff.entity.GroupDao;

public interface GroupRepository extends JpaRepository<GroupDao, Long>{

	Optional<GroupDao> findByName(String name);

}
