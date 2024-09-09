package com.gym.fitcliff.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.entity.CustomerDao.Gender;
import com.gym.fitcliff.entity.ImageDao;

public interface CustomerRepository extends JpaRepository<CustomerDao, Long> {

	Optional<CustomerDao> findByEmail(String email);

	@Query("SELECT c FROM CustomerDao c WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) "
			+ "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) "
			+ "AND (:phone IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) "
			+ "AND (:email IS NULL OR LOWER(c.email) = LOWER(:email)) " + "AND (:gender IS NULL OR c.gender = :gender) "
			+ "AND (:regDate IS NULL OR c.regDate = :regDate) " + "AND (:lastDate IS NULL OR c.lastDate = :lastDate) "
			+ "AND (:birthdate IS NULL OR c.birthdate = :birthdate) "
			+ "AND (:address IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) "
			+ "AND (:membershipAmount IS NULL OR c.membershipAmount = :membershipAmount) "
			+ "AND (:joinDate IS NULL OR c.joinDate = :joinDate) ")
	List<CustomerDao> searchCustomers(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("phone") String phone, @Param("email") String email, @Param("gender") Gender gender,
			@Param("regDate") LocalDate regDate, @Param("lastDate") LocalDate lastDate,
			@Param("birthdate") LocalDate birthdate, @Param("address") String address,
			@Param("membershipAmount") String membershipAmount, @Param("joinDate") LocalDate joinDate);

	@Query(value = "SELECT * FROM public.customer WHERE customer_text_idx @@ phraseto_tsquery('english', :searchPhrase)", nativeQuery = true)
	List<CustomerDao> searchCustomers(@Param("searchPhrase") String searchPhrase);

	@Query("SELECT c FROM CustomerDao c JOIN c.payments p WHERE p.membershipDuration.id = :membershipDurationId")
	List<CustomerDao> findAllByMembershipDuration(@Param("membershipDurationId") Long membershipDurationId);

	List<CustomerDao> findByIsActiveTrue();
	
	List<CustomerDao> findByIsActiveFalse();
	
	// Find all customers where lastDate is before today and they are still active
    List<CustomerDao> findByLastDateBeforeAndIsActiveTrue(LocalDate date);
    
 // Find all customers where lastDate is after or equal to today and they are not active
    List<CustomerDao> findByLastDateGreaterThanEqualAndIsActiveFalse(LocalDate date);
    
    @Query("SELECT c.image FROM CustomerDao c WHERE c.lastDate <= :oneYearAgo AND c.image.isActive = true")
    List<ImageDao> findImagesByLastDateOlderThanOneYearAndActiveImage(@Param("oneYearAgo") LocalDate oneYearAgo);

}