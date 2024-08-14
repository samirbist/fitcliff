package com.gym.fitcliff.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.mapper.CustomerDaoToDtoMapper;
import com.gym.fitcliff.mapper.CustomerToDaoMapper;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.repository.CustomerRepository;
import com.gym.fitcliff.service.CustomerMgmtService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerMgmtServiceImpl implements CustomerMgmtService {

	@Autowired
	private CustomerDaoToDtoMapper customerDaoToDtoMapper;
	
	@Autowired
	private CustomerToDaoMapper customerToDaoMapper;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer saveCustomer(final Customer customer) {
		final CustomerDao customerDao = customerDaoToDtoMapper.convertCustomerDtoToDao(customer);
		customerDao.getPhones().forEach(phone -> phone.setCustomer(customerDao));
		customerDao.setActive(true);
		final CustomerDao savedCustomer = customerRepository.save(customerDao);
		log.debug("Customer is saved {}", savedCustomer);
		return customerToDaoMapper.convertCustomerDaoToDto(savedCustomer);

	}

	@Override
	public Customer getCustomer(Long id) {
		   final Optional<CustomerDao> customerOptional = customerRepository.findById(id);
		    if (customerOptional.isPresent()) {
		    	Customer customer = customerToDaoMapper.convertCustomerDaoToDto(customerOptional.get());
		      return customer;
		    }
		    log.error("User not found for id : {}", id);
		 
		    throw new EntityNotFoundException("Customer Not found : "  + id);
	}

	@Override
	public List<Customer> getCustomers() {
		  final List<CustomerDao> customerDaoList = customerRepository.findAll();
		    final List<Customer> customerList =
		    		customerDaoList.stream()
		            .map(customerDao -> customerToDaoMapper.convertCustomerDaoToDto(customerDao))
		            .sorted(Comparator.comparing(Customer::getFirstName))
		            .collect(Collectors.toList());

		    return customerList;
	}
}
