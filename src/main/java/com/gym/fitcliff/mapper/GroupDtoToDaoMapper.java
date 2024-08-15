package com.gym.fitcliff.mapper;

import org.springframework.stereotype.Component;

import com.gym.fitcliff.entity.GroupDao;
import com.gym.fitcliff.model.Group;

@Component
public class GroupDtoToDaoMapper {

	public GroupDao convertGroupDtoToDao(Group group) {
		GroupDao dao = new GroupDao();
		dao.setId(group.getId());
		dao.setName(group.getName());
		dao.setDate(group.getDate());
		return dao;
	}

	
}
