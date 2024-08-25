package com.gym.fitcliff.mapper;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.GroupDao;
import com.gym.fitcliff.entity.GroupPaymentDao;
import com.gym.fitcliff.model.Group;
import com.gym.fitcliff.model.GroupPayment;

@Component
public class GroupDaoToDtoMapper {

	public Group convert(GroupDao groupDao) {
		Group group = new Group();
		group.setId(groupDao.getId());
		group.setName(groupDao.getName());
		group.setDate(groupDao.getDate());
		groupDao.getPayments().forEach(payment -> group.addPaymentsItem(toPaymentDto(payment)));
		return group;
	}

	private GroupPayment toPaymentDto(GroupPaymentDao payment) {
		GroupPayment groupPayment = new GroupPayment();
		groupPayment.setId(payment.getId());
		groupPayment.setGroupId(payment.getGroup().getId());
		groupPayment.setDate(payment.getDate());
		groupPayment.setAmount(payment.getAmount());
		groupPayment.setPendingAmount(payment.getPendingAmount());
		groupPayment.setPaymentType(GroupPayment.PaymentTypeEnum.valueOf(payment.getPaymentType().name()));
		return groupPayment;
	}

}
