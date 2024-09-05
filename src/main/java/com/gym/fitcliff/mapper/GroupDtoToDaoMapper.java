package com.gym.fitcliff.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.GroupDao;
import com.gym.fitcliff.entity.GroupPaymentDao;
import com.gym.fitcliff.model.Group;
import com.gym.fitcliff.model.GroupPayment;

@Component
public class GroupDtoToDaoMapper {

	public GroupDao convert(Group group) {
		GroupDao dao = new GroupDao();
		dao.setId(group.getId());
		dao.setName(group.getName());
		dao.setDate(group.getDate());
		dao.setPayments(new ArrayList<GroupPaymentDao>());
		dao.setMembershipAmount(group.getMembershipAmount());
		dao.setMembershipDuration(groupEnumToMembershipDuration(group.getMembershipDuration()));
		group.getPayments().forEach(payment -> dao.addGroupPayment(toGroupPaymentDao(payment)));
		return dao;
	}

	private GroupDao.MembershipDuration groupEnumToMembershipDuration(
			Group.MembershipDurationEnum membershipDurationEnum) {
		if (membershipDurationEnum == null) {
			return null;
		}
		return GroupDao.MembershipDuration.valueOf(membershipDurationEnum.name());
	}

	private GroupPaymentDao toGroupPaymentDao(GroupPayment payment) {
		GroupPaymentDao dao = new GroupPaymentDao();
		dao.setId(payment.getId());
//		dao.setGroup(payment.getGroupId());
		dao.setDate(payment.getDate());
		dao.setAmount(payment.getAmount());
		dao.setPendingAmount(payment.getPendingAmount());
		dao.setPaymentType(GroupPaymentDao.PaymentType.valueOf(payment.getPaymentType().name()));
		return dao;
	}

}
