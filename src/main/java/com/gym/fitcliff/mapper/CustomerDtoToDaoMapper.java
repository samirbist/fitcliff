package com.gym.fitcliff.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.entity.CustomerDao.Gender;
import com.gym.fitcliff.entity.ImageDao;
import com.gym.fitcliff.entity.IndividualPaymentDao;
import com.gym.fitcliff.entity.MemberShipDurationDao;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.Customer.GenderEnum;
import com.gym.fitcliff.model.IndividualPayment;

@Component
public class CustomerDtoToDaoMapper {
	
	public CustomerDao convert(Customer customer) {
		CustomerDao dao = new CustomerDao();
		dao.setId(customer.getId());
		dao.setFirstName(customer.getFirstName());
		dao.setLastName(customer.getLastName());
		dao.setEmail(customer.getEmail());
		dao.setPhone(customer.getPhone());
		dao.setGender(genderToGenderDao(customer.getGender()));
		dao.setActive(customer.getIsActive());
		dao.setRegDate(customer.getRegDate());
		dao.setJoinDate(customer.getJoinDate());
		dao.setLastDate(customer.getLastDate());
		dao.setBirthdate(customer.getBirthdate());
		dao.setAddress(customer.getAddress());
		dao.setMembershipAmount(customer.getMembershipAmount());
		dao.setPayments(indPaymentToIndPaymentDao(customer.getPayments()));
		dao.setImage(imageToImageDao(customer.getImage()));
		return dao;
	}
	
	private Gender genderToGenderDao(GenderEnum genderEnum) {
		if (genderEnum == null) {
			return null;
		}
		return Gender.valueOf(genderEnum.name());
	}
	

	private ImageDao imageToImageDao(Long imageId) {
		if (imageId == null) {
			return null;
		}
		ImageDao imageDao = new ImageDao();
		imageDao.setId(imageId);
		return imageDao;
	}

	private List<IndividualPaymentDao> indPaymentToIndPaymentDao(List<IndividualPayment> payments) {
		if (payments == null || payments.isEmpty()) {
			return List.of(); // Return an empty list if the input is empty
		}

		return payments.stream().map(payment -> {
			IndividualPaymentDao paymentDao = new IndividualPaymentDao();
			paymentDao.setId(payment.getId());
			paymentDao.setDate(payment.getDate());
			paymentDao.setAmount(payment.getAmount());
			paymentDao.setPendingAmount(payment.getPendingAmount());
			paymentDao.setPaymentType(IndividualPaymentDao.PaymentType.valueOf(payment.getPaymentType().name()));
			MemberShipDurationDao memberShipDurationDao = new MemberShipDurationDao();
			memberShipDurationDao.setId(payment.getMembershipDuration());
			paymentDao.setMembershipDuration(memberShipDurationDao);
			return paymentDao;
		}).collect(Collectors.toList());
	}

}
