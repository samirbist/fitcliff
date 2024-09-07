package com.gym.fitcliff.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.entity.CustomerDao.Gender;

public interface CustomerRepository extends JpaRepository<CustomerDao, Long> {

	Optional<CustomerDao> findByEmail(String email);

	@Query("SELECT c FROM CustomerDao c WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) "
			+ "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) "
			+ "AND (:phone IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) "
			+ "AND (:email IS NULL OR LOWER(c.email) = LOWER(:email)) " + "AND (:gender IS NULL OR c.gender = :gender) "
			+ "AND (:regDate IS NULL OR c.regDate = :regDate) " + "AND (:lastDate IS NULL OR c.lastDate = :lastDate) "
			+ "AND (:birthdate IS NULL OR c.birthdate = :birthdate) "
			+ "AND (:address IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) "
			+ "AND (:membershipAmount IS NULL OR c.membershipAmount = :membershipAmount)  ")
	List<CustomerDao> searchCustomers(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("phone") String phone, @Param("email") String email, @Param("gender") Gender gender,
			@Param("regDate") LocalDate regDate, @Param("lastDate") LocalDate lastDate,
			@Param("birthdate") LocalDate birthdate, @Param("address") String address,
			@Param("membershipAmount") String membershipAmount);

	@Query(value = "SELECT * FROM public.customer WHERE customer_text_idx @@ phraseto_tsquery('english', :searchPhrase)", nativeQuery = true)
	List<CustomerDao> searchCustomers(@Param("searchPhrase") String searchPhrase);
}