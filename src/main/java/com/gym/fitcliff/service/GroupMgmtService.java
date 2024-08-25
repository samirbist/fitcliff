package com.gym.fitcliff.service;

import java.util.List;

import com.gym.fitcliff.model.Group;

public interface GroupMgmtService {

	Group createGroup(Group group);

	List<Group> getGroup();

	Group updateGroup(Group group);

}
