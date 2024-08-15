package com.gym.fitcliff.mapper;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.GroupDao;
import com.gym.fitcliff.model.Group;

@Component
public class GroupDaoToDtoMapper {

	public Group convertGroupDaoToDto(GroupDao groupDao) {
		Group group = new Group();
		group.setId(groupDao.getId());
		group.setName(groupDao.getName());
		group.setDate(groupDao.getDate());
		return group;
	}

}
