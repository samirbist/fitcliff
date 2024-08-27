package com.gym.fitcliff.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gym.fitcliff.entity.UserDao;
import com.gym.fitcliff.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserDao> userDaoOptional = userRepository.findByUserName(username);
		if (userDaoOptional.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}
		UserDao user = userDaoOptional.get();

		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getName());
		List<SimpleGrantedAuthority> roleList = new ArrayList<>();
		roleList.add(grantedAuthority);
		return new org.springframework.security.core.userdetails.User(user.getUserName(), passwordEncoder.encode(user.getPassword()), roleList);
	}
}
