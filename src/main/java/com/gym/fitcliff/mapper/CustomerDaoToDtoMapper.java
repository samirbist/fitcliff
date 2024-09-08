package com.gym.fitcliff.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.entity.IndividualPaymentDao;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.Customer.GenderEnum;
import com.gym.fitcliff.model.IndividualPayment;

@Component
public class CustomerDaoToDtoMapper {

	public Customer convert(CustomerDao customerDao) {
		Customer customer = new Customer();
		customer.setId(customerDao.getId());
		customer.setFirstName(customerDao.getFirstName());
		customer.setLastName(customerDao.getLastName());
		customer.setEmail(customerDao.getEmail());
		customer.setPhone(customerDao.getPhone());
		customer.setGender(genderDaoToGender(customerDao.getGender()));
		customer.setIsActive(customerDao.isActive());
		customer.setRegDate(customerDao.getRegDate());
		customer.setLastDate(customerDao.getLastDate());
		customer.setBirthdate(customerDao.getBirthdate());
		customer.setJoinDate(customerDao.getJoinDate());
		customer.setAddress(customerDao.getAddress());
		customer.setMembershipAmount(customerDao.getMembershipAmount());
		customer.setPayments(indPaymentDaoListToIndPaymentList(customerDao.getPayments()));
		customer.setImage(customerDao.getImage().getId());
		return customer;
	}



	private List<IndividualPayment> indPaymentDaoListToIndPaymentList(List<IndividualPaymentDao> payments) {
		if (payments == null || payments.isEmpty()) {
			return List.of();
		}
		return payments.stream().map(this::convertIndividualPaymentDaoToIndividualPayment).collect(Collectors.toList());
	}

	private IndividualPayment convertIndividualPaymentDaoToIndividualPayment(IndividualPaymentDao paymentDao) {
		if (paymentDao == null) {
			return null;
		}
		IndividualPayment payment = new IndividualPayment();
		payment.setId(paymentDao.getId());
		payment.setDate(paymentDao.getDate());
		payment.setAmount(paymentDao.getAmount());
		payment.setPendingAmount(paymentDao.getPendingAmount());
		payment.setPaymentType(IndividualPayment.PaymentTypeEnum.valueOf(paymentDao.getPaymentType().name()));
		payment.setCutomerId(paymentDao.getCustomer().getId());
		payment.setMembershipDuration(paymentDao.getMembershipDuration().getId());
		return payment;
	}

	private GenderEnum genderDaoToGender(com.gym.fitcliff.entity.CustomerDao.Gender genderDao) {
		if (genderDao == null) {
			return null;
		}
		return GenderEnum.valueOf(genderDao.name());
	}



}
