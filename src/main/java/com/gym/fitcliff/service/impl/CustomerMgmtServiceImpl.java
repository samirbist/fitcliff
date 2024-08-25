package com.gym.fitcliff.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.entity.DocumentImageDao;
import com.gym.fitcliff.entity.GroupDao;
import com.gym.fitcliff.entity.ImageDao;
import com.gym.fitcliff.exception.CustomerException;
import com.gym.fitcliff.mapper.CustomerDtoToDaoMapper;
import com.gym.fitcliff.mapper.CustomerDaoToDtoMapper;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.SearchCustomer;
import com.gym.fitcliff.repository.CustomerRepository;
import com.gym.fitcliff.repository.DocumentImageRepository;
import com.gym.fitcliff.repository.GroupRepository;
import com.gym.fitcliff.repository.ImageRepository;
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

	@Autowired
	private DocumentImageRepository documentImageRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Override
	@Transactional
	public Customer saveCustomer(final Customer customer) {
		final CustomerDao customerDao = customerDtoToDaoMapper.convert(customer);
		customerDao.getPhones().forEach(phone -> phone.setCustomer(customerDao));
		customerDao.setActive(true);

		customerDao.getPayments().forEach(payment -> payment.setCustomer(customerDao));

		Optional<DocumentImageDao> documentDaoOptional = documentImageRepository.findById(customer.getDocumentImage());

		if (documentDaoOptional.isPresent()) {
			final DocumentImageDao documentImageDao = documentDaoOptional.get();
			customerDao.setDocumentImage(documentImageDao);
			Optional<ImageDao> imageDaoOptional = imageRepository.findById(customer.getImage());

			if (imageDaoOptional.isPresent()) {
				final ImageDao imageDao = imageDaoOptional.get();
				customerDao.setImage(imageDao);
				Optional<GroupDao> groupDaoOptional = groupRepository.findById(customer.getGroup().getId());
				if (groupDaoOptional.isPresent()) {
					customerDao.setGroup(groupDaoOptional.get());
					final CustomerDao savedCustomer = customerRepository.save(customerDao);
					log.debug("Customer is saved {}", savedCustomer);
					return customerDaoToDtoMapper.convert(savedCustomer);
				}
			}

		}

		throw new CustomerException("Error in creating customer", customer.getFirstName());

	}
//	private boolean IsUniqueEmail(final Customer customer) {
//		final Optional<CustomerDao> customerDaoOptional = customerRepository.findByEmail(customer.getEmail());
//		return customerDaoOptional.isEmpty();
//	}

	@Override
	public Customer getCustomer(Long id) {
		final Optional<CustomerDao> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = customerDaoToDtoMapper.convert(customerOptional.get());
			return customer;
		}
		log.error("User not found for id : {}", id);

		throw new EntityNotFoundException("Customer Not found : " + id);
	}

	@Override
	public List<Customer> getCustomers() {
		final List<CustomerDao> customerDaoList = customerRepository.findAll();
		final List<Customer> customerList = customerDaoList.stream()
				.map(customerDao -> customerDaoToDtoMapper.convert(customerDao))
				.sorted(Comparator.comparing(Customer::getFirstName)).collect(Collectors.toList());

		return customerList;
	}

	@Override
	@Transactional
	public Customer updateCustomer(Customer customer) {
		CustomerDao savedCustomerDao = customerDtoToDaoMapper.convert(customer);
		final Optional<CustomerDao> customerOptional = customerRepository.findById(customer.getId());
		if (customerOptional.isPresent()) {
			final CustomerDao customerDao = customerOptional.get();
			customerDao.getPhones().clear();
			customerDao.getPayments().clear();
			customerRepository.save(customerDao);
			customerDao.setFirstName(savedCustomerDao.getFirstName());
			customerDao.setLastName(savedCustomerDao.getLastName());
			savedCustomerDao.getPhones().forEach(customerDao::addPhone);
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
			return customerDaoToDtoMapper.convert(savedCustomerDao);
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
		if (customerDaoList != null && !customerDaoList.isEmpty()) {
			final List<Customer> customerList = customerDaoList.stream()
					.map(customerDaoRet -> customerDaoToDtoMapper.convert(customerDaoRet))
					.sorted(Comparator.comparing(Customer::getFirstName)).collect(Collectors.toList());

			return customerList;
		} else {
			throw new EntityNotFoundException("Customer Not found ");
		}
	}

	@Override
	public List<Customer> searchCustomersBy(String text) {
		List<CustomerDao> customerDaoList = customerRepository.searchCustomers(text);
		if (customerDaoList != null && !customerDaoList.isEmpty()) {
			final List<Customer> customerList = customerDaoList.stream()
					.map(customerDaoRet -> customerDaoToDtoMapper.convert(customerDaoRet))
					.sorted(Comparator.comparing(Customer::getFirstName)).collect(Collectors.toList());

			return customerList;
		} else {
			return List.of();
		}
	}
}
