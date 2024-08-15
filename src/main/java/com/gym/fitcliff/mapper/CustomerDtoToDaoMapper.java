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
import com.gym.fitcliff.entity.PhoneDao;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.Customer.GenderEnum;
import com.gym.fitcliff.model.Customer.MembershipDurationEnum;
import com.gym.fitcliff.model.Customer.MembershipTypeEnum;
import com.gym.fitcliff.model.DocumentImage;
import com.gym.fitcliff.model.Group;
import com.gym.fitcliff.model.Image;
import com.gym.fitcliff.model.IndividualPayment;
import com.gym.fitcliff.model.SearchCustomer;

@Component
public class CustomerDtoToDaoMapper {
	
	public CustomerDao convertCustomerDtoToDao(Customer customer) {
		CustomerDao dao = new CustomerDao();
		dao.setId(customer.getId());
		dao.setFirstName(customer.getFirstName());
		dao.setLastName(customer.getLastName());
		dao.setEmail(customer.getEmail());
		dao.setPhones(phoneListToPhoneDaoList(customer.getPhones()));
		dao.setPhotoMongoId(customer.getPhotoMongoId());
		dao.setDocMongoId(customer.getDocMongoId());
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

	private List<PhoneDao> phoneListToPhoneDaoList(List<String> phones) {
		if (phones == null || phones.isEmpty()) {
			return List.of(); // Return an empty list if the input is empty
		}

		return phones.stream().map(phone -> {
			PhoneDao dao = new PhoneDao();
			dao.setNumber(phone);
			return dao;
		}).collect(Collectors.toList());
	}

	private GroupDao groupToGroupDao(Group group) {
		if (group == null) {
			return null;
		}
		GroupDao groupDao = new GroupDao();
		groupDao.setId(group.getId());
		groupDao.setName(group.getName());
		groupDao.setDate(group.getDate());
		return groupDao;
	}

	private DocumentImageDao documentImageToDocumentImageDao(DocumentImage documentImage) {
		if (documentImage == null) {
			return null;
		}
		DocumentImageDao documentImageDao = new DocumentImageDao();
		documentImageDao.setId(documentImage.getId());
		// Set other properties from DocumentImage to DocumentImageDao if needed
		return documentImageDao;
	}

	private ImageDao imageToImageDao(Image image) {
		if (image == null) {
			return null;
		}
		ImageDao imageDao = new ImageDao();
		imageDao.setId(image.getId());
		// Set other properties from Image to ImageDao if needed
		return imageDao;
	}

	private List<IndividualPaymentDao> indPaymentToIndPaymentDao(List<IndividualPayment> payments) {
		if (payments == null || payments.isEmpty()) {
			return List.of(); // Return an empty list if the input is empty
		}

		return payments.stream().map(payment -> {
			IndividualPaymentDao paymentDao = new IndividualPaymentDao();
			paymentDao.setId(payment.getId());
			// Set other properties from IndividualPayment to IndividualPaymentDao if needed
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
		dao.setMembershipAmount(customer.getMembershipAmount());
		dao.setMembershipDuration(searchEnumToMembershipDuration(customer.getMembershipDuration()));
		return dao;
	}

}
