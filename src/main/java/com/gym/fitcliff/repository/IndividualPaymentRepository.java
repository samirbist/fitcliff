package com.gym.fitcliff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.fitcliff.entity.IndividualPaymentDao;
import com.gym.fitcliff.entity.MemberShipDurationDao;

public interface IndividualPaymentRepository extends JpaRepository<IndividualPaymentDao, Long>{

   List<IndividualPaymentDao> findByMembershipDuration(MemberShipDurationDao memberShipDurationDao);
}
