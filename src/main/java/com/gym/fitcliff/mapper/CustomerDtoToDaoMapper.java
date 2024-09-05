package com.gym.fitcliff.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.entity.CustomerDao.Gender;
import com.gym.fitcliff.entity.CustomerDao.MembershipDuration;
import com.gym.fitcliff.entity.CustomerDao.MembershipType;
import com.gym.fitcliff.entity.DocumentImageDao;
import com.gym.fitcliff.entity.GroupDao;
import com.gym.fitcliff.entity.ImageDao;
import com.gym.fitcliff.entity.IndividualPaymentDao;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.Customer.GenderEnum;
import com.gym.fitcliff.model.Customer.MembershipDurationEnum;
import com.gym.fitcliff.model.Customer.MembershipTypeEnum;
import com.gym.fitcliff.model.Group;
import com.gym.fitcliff.model.IndividualPayment;
import com.gym.fitcliff.model.SearchCustomer;

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
		dao.setBirthdate(customer.getBirthdate());
		dao.setAddress(customer.getAddress());
		dao.setMembershipAmount(customer.getMembershipAmount());
		dao.setMembershipDuration(enumToMembershipDuration(customer.getMembershipDuration()));
		dao.setPayments(indPaymentToIndPaymentDao(customer.getPayments()));
		dao.setGroup(groupToGroupDao(customer.getGroup()));
		dao.setMembershipType(enumToMembershipType(customer.getMembershipType()));
		dao.setDocumentImage(documentImageToDocumentImageDao(customer.getDocumentImage()));
		dao.setImage(imageToImageDao(customer.getImage()));
		return dao;
	}



	private MembershipType enumToMembershipType(MembershipTypeEnum membershipTypeEnum) {
		if (membershipTypeEnum == null) {
			return null;
		}
		return MembershipType.valueOf(membershipTypeEnum.name());
	}

	private MembershipDuration enumToMembershipDuration(MembershipDurationEnum membershipDurationEnum) {
		if (membershipDurationEnum == null) {
			return null;
		}
		return MembershipDuration.valueOf(membershipDurationEnum.name());
	}
	
	
	private MembershipDuration searchEnumToMembershipDuration(SearchCustomer.MembershipDurationEnum membershipDurationEnum) {
		if (membershipDurationEnum == null) {
			return null;
		}
		return MembershipDuration.valueOf(membershipDurationEnum.name());
	}

	
	private Gender genderToGenderDao(GenderEnum genderEnum) {
		if (genderEnum == null) {
			return null;
		}
		return Gender.valueOf(genderEnum.name());
	}
	
	private Gender genderSearchToGender(SearchCustomer.GenderEnum genderEnum) {
		if (genderEnum == null) {
			return null;
		}
		return Gender.valueOf(genderEnum.name());
	}

	private GroupDao groupToGroupDao(Group group) {
		if (group == null) {
			return null;
		}
		GroupDao groupDao = new GroupDao();
		groupDao.setId(group.getId());
		groupDao.setName(group.getName());
		groupDao.setDate(group.getDate());
		groupDao.setMembershipAmount(group.getMembershipAmount());
//		groupDao.setMembershipDuration(groupEnumToMembershipDuration(group.getMembershipDuration()));
		return groupDao;
	}
	
//	private GroupDao.MembershipDuration groupEnumToMembershipDuration(Group.MembershipDurationEnum membershipDurationEnum) {
//		if (membershipDurationEnum == null) {
//			return null;
//		}
//		return GroupDao.MembershipDuration.valueOf(membershipDurationEnum.name());
//	}

	private DocumentImageDao documentImageToDocumentImageDao(Long documentImage) {
		if (documentImage == null) {
			return null;
		}
		DocumentImageDao documentImageDao = new DocumentImageDao();
		documentImageDao.setId(documentImage);
		return documentImageDao;
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
			return paymentDao;
		}).collect(Collectors.toList());
	}



	public CustomerDao convertSearchCustomerToDao(SearchCustomer customer) {
		CustomerDao dao = new CustomerDao();
		dao.setFirstName(customer.getFirstName());
		dao.setLastName(customer.getLastName());
		dao.setEmail(customer.getEmail());
		dao.setGender(genderSearchToGender(customer.getGender()));
		dao.setRegDate(customer.getRegDate());
		dao.setJoinDate(customer.getJoinDate());
		dao.setBirthdate(customer.getBirthdate());
		dao.setAddress(customer.getAddress());
		dao.setPhone(customer.getPhone());
		dao.setMembershipAmount(customer.getMembershipAmount());
		dao.setMembershipDuration(searchEnumToMembershipDuration(customer.getMembershipDuration()));
		return dao;
	}

}
