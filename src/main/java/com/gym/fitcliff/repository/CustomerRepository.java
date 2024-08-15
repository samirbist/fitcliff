package com.gym.fitcliff.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.entity.CustomerDao.Gender;
import com.gym.fitcliff.entity.CustomerDao.MembershipDuration;

public interface CustomerRepository extends JpaRepository<CustomerDao, Long> {

	Optional<CustomerDao> findByEmail(String email);

	@Query("SELECT c FROM CustomerDao c " + "WHERE (:firstName IS NULL OR c.firstName LIKE %:firstName%) "
			+ "AND (:lastName IS NULL OR c.lastName LIKE %:lastName%) " + "AND (:email IS NULL OR c.email = :email) "
			+ "AND (:gender IS NULL OR c.gender = :gender) " + "AND (:regDate IS NULL OR c.regDate = :regDate) "
			+ "AND (:joinDate IS NULL OR c.joinDate = :joinDate) "
			+ "AND (:birthdate IS NULL OR c.birthdate = :birthdate) "
			+ "AND (:address IS NULL OR c.address LIKE %:address%)"
			+ "AND (:membershipAmount IS NULL OR c.membershipAmount = :membershipAmount) "
			+ "AND (:membershipDuration IS NULL OR c.membershipDuration = :membershipDuration) ")
	List<CustomerDao> searchCustomers(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("email") String email, @Param("gender") Gender gender, @Param("regDate") LocalDate regDate,
			@Param("joinDate") LocalDate joinDate, @Param("birthdate") LocalDate birthdate,
			@Param("address") String address, @Param("membershipAmount") String membershipAmount,
			@Param("membershipDuration") MembershipDuration membershipDuration);
}