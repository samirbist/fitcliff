package com.gym.fitcliff.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.exception.CustomerException;
import com.gym.fitcliff.mapper.CustomerDtoToDaoMapper;
import com.gym.fitcliff.mapper.CustomerDaoToDtoMapper;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.SearchCustomer;
import com.gym.fitcliff.repository.CustomerRepository;
import com.gym.fitcliff.service.CustomerMgmtService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerMgmtServiceImpl implements CustomerMgmtService {

	@Autowired
	private CustomerDtoToDaoMapper customerDtoToDaoMapper;

	@Autowired
	private CustomerDaoToDtoMapper customerDaoToDtoMapper;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer saveCustomer(final Customer customer) {
		if (IsUniqueEmail(customer)) {
			final CustomerDao customerDao = customerDtoToDaoMapper.convertCustomerDtoToDao(customer);
			customerDao.getPhones().forEach(phone -> phone.setCustomer(customerDao));
			customerDao.setActive(true);
			final CustomerDao savedCustomer = customerRepository.save(customerDao);
			log.debug("Customer is saved {}", savedCustomer);
			return customerDaoToDtoMapper.convertCustomerDaoToDto(savedCustomer);
		} else {
			log.error("Error in saving user as username already exists {}", customer.getFirstName());
			throw new CustomerException("Customer email already exists", customer.getEmail());
		}
	}

	private boolean IsUniqueEmail(final Customer customer) {
		final Optional<CustomerDao> customerDaoOptional = customerRepository.findByEmail(customer.getEmail());
		return customerDaoOptional.isEmpty();
	}

	@Override
	public Customer getCustomer(Long id) {
		final Optional<CustomerDao> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = customerDaoToDtoMapper.convertCustomerDaoToDto(customerOptional.get());
			return customer;
		}
		log.error("User not found for id : {}", id);

		throw new EntityNotFoundException("Customer Not found : " + id);
	}

	@Override
	public List<Customer> getCustomers() {
		final List<CustomerDao> customerDaoList = customerRepository.findAll();
		final List<Customer> customerList = customerDaoList.stream()
				.map(customerDao -> customerDaoToDtoMapper.convertCustomerDaoToDto(customerDao))
				.sorted(Comparator.comparing(Customer::getFirstName)).collect(Collectors.toList());

		return customerList;
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		CustomerDao savedCustomerDao = customerDtoToDaoMapper.convertCustomerDtoToDao(customer);
		final Optional<CustomerDao> customerOptional = customerRepository.findById(customer.getId());
		if (customerOptional.isPresent()) {
			final CustomerDao customerDao = customerOptional.get();
			customerDao.getPhones().clear();
			customerDao.getPayments().clear();
			customerRepository.save(customerDao);
			customerDao.setFirstName(savedCustomerDao.getFirstName());
			customerDao.setLastName(savedCustomerDao.getLastName());
			savedCustomerDao.getPhones().forEach(customerDao::addPhone);
			customerDao.setPhotoMongoId(savedCustomerDao.getPhotoMongoId());
			customerDao.setDocMongoId(savedCustomerDao.getDocMongoId());
			customerDao.setGender(savedCustomerDao.getGender());
			customerDao.setActive(savedCustomerDao.isActive());
			customerDao.setRegDate(savedCustomerDao.getRegDate());
			customerDao.setJoinDate(savedCustomerDao.getJoinDate());
			customerDao.setBirthdate(savedCustomerDao.getBirthdate());
			customerDao.setAddress(savedCustomerDao.getAddress());
			customerDao.setMembershipAmount(savedCustomerDao.getMembershipAmount());
			customerDao.setMembershipDuration(savedCustomerDao.getMembershipDuration());
			savedCustomerDao.getPayments().forEach(customerDao::addIndividualPayment);
			customerDao.setGroup(savedCustomerDao.getGroup());
			customerDao.setMembershipType(savedCustomerDao.getMembershipType());
			customerDao.setDocumentImage(savedCustomerDao.getDocumentImage());
			customerDao.setImage(savedCustomerDao.getImage());
			savedCustomerDao = customerRepository.saveAndFlush(customerDao);
			return customerDaoToDtoMapper.convertCustomerDaoToDto(savedCustomerDao);
		}
		log.error("Customer not found for id : {}", customer.getId());
		throw new EntityNotFoundException("Customer Not found : " + customer.getId());
	}

	@Override
	public List<Customer> searchCustomers(SearchCustomer searchCustomer) {
		CustomerDao customerDao = customerDtoToDaoMapper.convertSearchCustomerToDao(searchCustomer);
		List<CustomerDao> customerDaoList = customerRepository.searchCustomers(customerDao.getFirstName(),
				customerDao.getLastName(), customerDao.getEmail(), customerDao.getGender(), customerDao.getRegDate(),
				customerDao.getJoinDate(), customerDao.getBirthdate(), customerDao.getAddress(),
				customerDao.getMembershipAmount(), customerDao.getMembershipDuration());
		if (customerDaoList != null  && !customerDaoList.isEmpty()) {
			final List<Customer> customerList = customerDaoList.stream()
					.map(customerDaoRet -> customerDaoToDtoMapper.convertCustomerDaoToDto(customerDaoRet))
					.sorted(Comparator.comparing(Customer::getFirstName)).collect(Collectors.toList());

			return customerList;
		} else {
			throw new EntityNotFoundException("Customer Not found ");
		}
	}
}
