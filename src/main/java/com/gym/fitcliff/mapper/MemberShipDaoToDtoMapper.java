package com.gym.fitcliff.mapper;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.MemberShipDurationDao;
import com.gym.fitcliff.model.MemberShipDuration;

@Component
public class MemberShipDaoToDtoMapper {

	public MemberShipDuration convert(MemberShipDurationDao memberShipDurationDao) {
		MemberShipDuration memberShipDuration = new MemberShipDuration();
		memberShipDuration.setId(memberShipDurationDao.getId());
		memberShipDuration.setDuration(memberShipDurationDao.getDuration());
		return memberShipDuration;
	}
}
