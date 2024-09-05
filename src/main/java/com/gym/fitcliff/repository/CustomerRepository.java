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

	@Query("SELECT c FROM CustomerDao c WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) "
			+ "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) "
			+ "AND (:phone IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) "
			+ "AND (:email IS NULL OR LOWER(c.email) = LOWER(:email)) " + "AND (:gender IS NULL OR c.gender = :gender) "
			+ "AND (:regDate IS NULL OR c.regDate = :regDate) " + "AND (:joinDate IS NULL OR c.joinDate = :joinDate) "
			+ "AND (:birthdate IS NULL OR c.birthdate = :birthdate) "
			+ "AND (:address IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) "
			+ "AND (:membershipAmount IS NULL OR c.membershipAmount = :membershipAmount) "
			+ "AND (:membershipDuration IS NULL OR c.membershipDuration = :membershipDuration) ")
	List<CustomerDao> searchCustomers(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("phone") String phone, @Param("email") String email, @Param("gender") Gender gender,
			@Param("regDate") LocalDate regDate, @Param("joinDate") LocalDate joinDate,
			@Param("birthdate") LocalDate birthdate, @Param("address") String address,
			@Param("membershipAmount") String membershipAmount,
			@Param("membershipDuration") MembershipDuration membershipDuration);

	@Query(value = "SELECT * FROM public.customer WHERE customer_text_idx @@ phraseto_tsquery('english', :searchPhrase)", nativeQuery = true)
	List<CustomerDao> searchCustomers(@Param("searchPhrase") String searchPhrase);
}