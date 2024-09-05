package com.gym.fitcliff.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.fitcliff.entity.GroupDao;
import com.gym.fitcliff.mapper.GroupDaoToDtoMapper;
import com.gym.fitcliff.mapper.GroupDtoToDaoMapper;
import com.gym.fitcliff.model.Group;
import com.gym.fitcliff.repository.GroupRepository;
import com.gym.fitcliff.service.GroupMgmtService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
		if (isGroupNameUnique(group.getName())) {
			GroupDao groupDao = groupDtoToDaoMapper.convert(group);
			if (groupDao.getDate() == null) {
				groupDao.setDate(LocalDate.now());
			}
			GroupDao savedGroupDao = groupRepository.save(groupDao);
			log.debug("Group is saved {}", savedGroupDao);
			return groupDaoToDtoMapper.convert(savedGroupDao);
		} else {
			log.error("Group name already exists for group {}", group);
			throw new EntityExistsException("Group name already exists");
		}
	}

	private boolean isGroupNameUnique(String name) {
		final Optional<GroupDao> groupDaoOptional = groupRepository.findByName(name);
		return groupDaoOptional.isEmpty();
	}

	@Override
	public List<Group> getGroup() {
		List<GroupDao> groupDaoList = groupRepository.findAll();
		final List<Group> groupList = groupDaoList.stream().map(groupDao -> groupDaoToDtoMapper.convert(groupDao))
				.sorted(Comparator.comparing(Group::getName)).collect(Collectors.toList());
		return groupList;
	}

	@Override
	public Group updateGroup(Group group) {
		GroupDao groupDao = groupDtoToDaoMapper.convert(group);
		Optional<GroupDao> groupDaoOptional = groupRepository.findById(group.getId());
		if (groupDaoOptional.isPresent()) {
			GroupDao savedGroupDao = groupDaoOptional.get();
			savedGroupDao.setName(groupDao.getName());
			savedGroupDao.setDate(groupDao.getDate());
			if (savedGroupDao.getPayments() != null) {
				savedGroupDao.getPayments().clear();
			}
			groupDao.getPayments().forEach(savedGroupDao::addGroupPayment);
			savedGroupDao = groupRepository.saveAndFlush(savedGroupDao);
			log.debug("Group is updated {}", savedGroupDao);
			return groupDaoToDtoMapper.convert(savedGroupDao);
		} else {
			throw new EntityNotFoundException("Group not found with id " + group.getId());
		}
	}

}
