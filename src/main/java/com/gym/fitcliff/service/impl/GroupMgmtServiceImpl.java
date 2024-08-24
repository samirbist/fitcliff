package com.gym.fitcliff.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.fitcliff.entity.GroupDao;
import com.gym.fitcliff.mapper.GroupDaoToDtoMapper;
import com.gym.fitcliff.mapper.GroupDtoToDaoMapper;
import com.gym.fitcliff.model.Group;
import com.gym.fitcliff.repository.GroupRepository;
import com.gym.fitcliff.service.GroupMgmtService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GroupMgmtServiceImpl implements GroupMgmtService {

	@Autowired
	private GroupDtoToDaoMapper groupDtoToDaoMapper;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private GroupDaoToDtoMapper groupDaoToDtoMapper;

	@Override
	public Group createGroup(Group group) {
		GroupDao groupDao = groupDtoToDaoMapper.convert(group);
		groupDao.setDate(LocalDate.now());
		GroupDao savedGroupDao = groupRepository.save(groupDao);
		log.debug("Group is saved {}", savedGroupDao);
		return groupDaoToDtoMapper.convert(savedGroupDao);
	}

	@Override
	public List<Group> getGroup() {
		List<GroupDao> groupDaoList = groupRepository.findAll();
		final List<Group> groupList = groupDaoList.stream()
				.map(groupDao -> groupDaoToDtoMapper.convert(groupDao))
				.sorted(Comparator.comparing(Group::getName)).collect(Collectors.toList());
		return groupList;
	}

}
