package com.gym.fitcliff.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.CustomerDao;
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


@Component
public class CustomerDaoToDtoMapper {
	
	public Customer convert(CustomerDao customerDao) {
		Customer customer = new Customer(); 
		customer.setId(customerDao.getId());
		customer.setFirstName(customerDao.getFirstName());
		customer.setLastName(customerDao.getLastName());
		customer.setEmail(customerDao.getEmail());
		customer.setPhones(phoneListToStringList(customerDao.getPhones())); 
		customer.setPhotoMongoId(customerDao.getPhotoMongoId());
		customer.setDocMongoId(customerDao.getDocMongoId());
		customer.setGender(genderDaoToGender(customerDao.getGender()));
		customer.setIsActive(customerDao.isActive());
		customer.setRegDate(customerDao.getRegDate());
		customer.setJoinDate(customerDao.getJoinDate());
		customer.setBirthdate(customerDao.getBirthdate());
		customer.setAddress(customerDao.getAddress());
		customer.setMembershipAmount(customerDao.getMembershipAmount());
		customer.setMembershipDuration(membershipDurationToEnum(customerDao.getMembershipDuration()));
		customer.setPayments(indPaymentDaoListToIndPaymentList(customerDao.getPayments()));
        customer.setGroup(groupDaoToGroup(customerDao.getGroup()));
        customer.setMembershipType(membershipTypeToEnum(customerDao.getMembershipType()));
        customer.setDocumentImage(documentImageDaoToDocumentImage(customerDao.getDocumentImage()));
        customer.setImage(imageDaoToImage(customerDao.getImage()));
		return customer;
	}
	
	private Image imageDaoToImage(ImageDao imageDao) {
        if (imageDao == null) {
            return null;
        }
        Image image = new Image();
        // Map other fields if necessary.
        return image;
    }
	
	private DocumentImage documentImageDaoToDocumentImage(DocumentImageDao documentImageDao) {
        if (documentImageDao == null) {
            return null;
        }
        DocumentImage documentImage = new DocumentImage();
        // Map other fields if necessary.
        return documentImage;
    }
	
	private MembershipTypeEnum membershipTypeToEnum(MembershipType membershipType) {
        if (membershipType == null) {
            return null;
        }
        return MembershipTypeEnum.valueOf(membershipType.name());
    }
	
	private List<IndividualPayment> indPaymentDaoListToIndPaymentList(List<IndividualPaymentDao> payments) {
        if (payments == null || payments.isEmpty()) {
            return List.of();
        }
        return payments.stream()
                       .map(this::convertIndividualPaymentDaoToIndividualPayment)
                       .collect(Collectors.toList());
    }
	private IndividualPayment convertIndividualPaymentDaoToIndividualPayment(IndividualPaymentDao paymentDao) {
        if (paymentDao == null) {
            return null;
        }
        IndividualPayment payment = new IndividualPayment();
        // Map other fields if necessary.
        return payment;
    }
	private GenderEnum genderDaoToGender(com.gym.fitcliff.entity.CustomerDao.Gender genderDao) {
        if (genderDao == null) {
            return null;
        }
        return GenderEnum.valueOf(genderDao.name());
    }
	
	
	private MembershipDurationEnum membershipDurationToEnum(MembershipDuration membershipDuration) {
        if (membershipDuration == null) {
            return null;
        }
        return MembershipDurationEnum.valueOf(membershipDuration.name());
    }

	

	private List<String> phoneListToStringList(List<PhoneDao> phones) {
        if (phones == null || phones.isEmpty()) {
            return List.of();
        }
        return phones.stream()
                     .map(PhoneDao::getNumber)
                     .collect(Collectors.toList());
    }

	
	private Group groupDaoToGroup(GroupDao groupDao) {
        if (groupDao == null) {
            return null;
        }
        Group group = new Group();
        group.setId(groupDao.getId());
        group.setName(groupDao.getName());
        group.setDate(groupDao.getDate());
        return group;
    }

}
