package com.gym.fitcliff.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.fitcliff.config.CustomerConfig;
import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.entity.ImageDao;
import com.gym.fitcliff.entity.IndividualPaymentDao;
import com.gym.fitcliff.entity.MemberShipDurationDao;
import com.gym.fitcliff.exception.CustomerException;
import com.gym.fitcliff.mapper.CustomerDaoToDtoMapper;
import com.gym.fitcliff.mapper.CustomerDtoToDaoMapper;
import com.gym.fitcliff.mapper.MemberShipDaoToDtoMapper;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.IndividualPayment;
import com.gym.fitcliff.model.MemberShipDuration;
import com.gym.fitcliff.repository.CustomerRepository;
import com.gym.fitcliff.repository.ImageRepository;
import com.gym.fitcliff.repository.MemberShipDurationRepository;
import com.gym.fitcliff.service.CustomerMgmtService;
import com.gym.fitcliff.service.ImageMgmtService;

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
	private ImageRepository imageRepository;

	@Autowired
	private ImageMgmtService imageMgmtService;

	@Autowired
	private MemberShipDurationRepository memberShipDurationRepository;

	@Autowired
	private CustomerConfig customerConfig;

	@Autowired
	private MemberShipDaoToDtoMapper memberShipDaoToDtoMapper;

	@Override
	@Transactional
	public Customer saveCustomer(final Customer customer) {
		final CustomerDao customerDao = customerDtoToDaoMapper.convert(customer);
		customerDao.getPayments().forEach(payment -> payment.setCustomer(customerDao));

		setMembershipDuration(customerDao);

		Optional<ImageDao> imageDaoOptional = imageRepository.findById(customer.getImage());

		if (imageDaoOptional.isPresent()) {
			final ImageDao imageDao = imageDaoOptional.get();
			customerDao.setImage(imageDao);
			final CustomerDao savedCustomer = customerRepository.save(customerDao);
			log.debug("Customer is saved {}", savedCustomer);
			return customerDaoToDtoMapper.convert(savedCustomer);
		}
		throw new CustomerException("Error in creating customer", customer.getFirstName());

	}

	private void setMembershipDuration(final CustomerDao customerDao) {
		customerDao.getPayments().forEach(payment -> {
			Long membershipDurationId = payment.getMembershipDuration().getId();
			Optional<MemberShipDurationDao> memberShipDurationDaoOption = memberShipDurationRepository
					.findById(membershipDurationId);
			if (memberShipDurationDaoOption.isPresent()) {
				payment.setMembershipDuration(memberShipDurationDaoOption.get());
			} else {
				throw new CustomerException("Error in creating customer Membership duration is incorrect ",
						String.valueOf(membershipDurationId));
			}

		});
	}

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
		final List<CustomerDao> customerDaoList = customerRepository.findByIsActiveTrue();
		final List<Customer> customerList = customerDaoList.stream()
				.map(customerDao -> customerDaoToDtoMapper.convert(customerDao))
				.sorted(Comparator.comparing(Customer::getFirstName)).collect(Collectors.toList());

		return customerList;
	}

	@Override
	@Transactional
	public Customer updateCustomer(Customer customer) {
		CustomerDao savedCustomerDao = customerDtoToDaoMapper.convert(customer);
		setMembershipDuration(savedCustomerDao);
		final Optional<CustomerDao> customerOptional = customerRepository.findById(customer.getId());
		if (customerOptional.isPresent()) {
			final CustomerDao customerDao = customerOptional.get();
			customerDao.getPayments().clear();
			customerDao.setFirstName(savedCustomerDao.getFirstName());
			customerDao.setLastName(savedCustomerDao.getLastName());
			customerDao.setPhone(savedCustomerDao.getPhone());
			customerDao.setEmail(savedCustomerDao.getEmail());
			customerDao.setGender(savedCustomerDao.getGender());
			customerDao.setActive(savedCustomerDao.isActive());
			customerDao.setRegDate(savedCustomerDao.getRegDate());
			customerDao.setLastDate(savedCustomerDao.getLastDate());
			customerDao.setJoinDate(savedCustomerDao.getJoinDate());
			customerDao.setBirthdate(savedCustomerDao.getBirthdate());
			customerDao.setAddress(savedCustomerDao.getAddress());
			customerDao.setMembershipAmount(savedCustomerDao.getMembershipAmount());
			savedCustomerDao.getPayments().forEach(customerDao::addIndividualPayment);

			Optional<ImageDao> imageDaoOptional = imageRepository.findById(customer.getImage());
			if (imageDaoOptional.isPresent()) {
				final ImageDao imageDao = imageDaoOptional.get();
				Long existingImageId = customerDao.getImage().getId();
				customerDao.setImage(imageDao);
				savedCustomerDao = customerRepository.saveAndFlush(customerDao);

				if (!customer.getImage().equals(existingImageId)) {
					imageMgmtService.deleteImage(existingImageId);
				}
				return customerDaoToDtoMapper.convert(savedCustomerDao);
			} else {
				throw new EntityNotFoundException("Image id does not exists :" + customer.getImage());
			}
		}

		log.error("Customer not found for id : {}", customer.getId());
		throw new EntityNotFoundException("Customer Not found : " + customer.getId());
	}

	@Override
	public List<Customer> searchCustomers(Customer searchCustomer) {
		CustomerDao customerDao = customerDtoToDaoMapper.convert(searchCustomer);
		List<CustomerDao> customerDaoList = customerRepository.searchCustomers(customerDao.getFirstName(),
				customerDao.getLastName(), customerDao.getPhone(), customerDao.getEmail(), customerDao.getGender(),
				customerDao.getRegDate(), customerDao.getLastDate(), customerDao.getBirthdate(),
				customerDao.getAddress(), customerDao.getMembershipAmount(), customerDao.getJoinDate());
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
		final List<Customer> customerList = customerDaoList.stream()
				.map(customerDaoRet -> customerDaoToDtoMapper.convert(customerDaoRet))
				.sorted(Comparator.comparing(Customer::getFirstName)).collect(Collectors.toList());

		return customerList;

	}

	@Override
	public List<Customer> getCustomerByDuration(Long membershipDurationId) {
		// Fetch all customers
		List<CustomerDao> customerDaoList = customerRepository.findByIsActiveTrue();

		// Convert CustomerDao to Customer DTO
		List<Customer> customerList = customerDaoList.stream()
				.map(customerDao -> customerDaoToDtoMapper.convert(customerDao)).collect(Collectors.toList());

		// Sort payments by date for each customer
		customerList.forEach(customer -> {
			List<IndividualPayment> sortedPayments = customer.getPayments().stream()
					.sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate())) // Sort payments by date (most recent
																				// first)
					.collect(Collectors.toList());
			customer.setPayments(sortedPayments); // Set sorted payments
		});

		// Filter customers based on the most recent payment's membership duration
		return customerList.stream().filter(customer -> {
			// Get the most recent payment
			IndividualPayment mostRecentPayment = customer.getPayments().stream().findFirst() // Get the first element
																								// (most recent due to
																								// sorting)
					.orElse(null);

			// Check if the most recent payment exists and if its membership duration
			// matches the provided ID
			return mostRecentPayment != null && mostRecentPayment.getMembershipDuration().equals(membershipDurationId);
		}).collect(Collectors.toList());
	}

	@Override
	public List<Customer> getCustomersInactiveSoon() {
		List<CustomerDao> activeCustomers = customerRepository.findByIsActiveTrue();
		LocalDate today = LocalDate.now();

		// Sort payments by date for each customer
		activeCustomers.forEach(customer -> {
			List<IndividualPaymentDao> sortedPayments = customer.getPayments().stream()
					.sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate())) // Sort payments by date (most recent
																				// first)
					.collect(Collectors.toList());
			customer.setPayments(sortedPayments); // Set sorted payments
		});

		// Filter customers based on the most recent payment's membership duration
		activeCustomers = activeCustomers.stream().filter(customer -> {
			// Get the most recent payment
			IndividualPaymentDao mostRecentPayment = customer.getPayments().stream().findFirst() // Get the first
																									// element (most
																									// recent due to
																									// sorting)
					.orElse(null);
			if (mostRecentPayment == null) {
				return false;
			}
			LocalDate expiryDate = customer.getLastDate();

			LocalDate cutOffDate = expiryDate.minusDays(customerConfig.getDaysBeforeExpiry());

			// Check if the most recent payment exists and if its membership duration
			// matches the provided ID
			return expiryDate.isAfter(today) && cutOffDate.isBefore(today);

		}).collect(Collectors.toList());

		return activeCustomers.stream().map(customerDao -> customerDaoToDtoMapper.convert(customerDao))
				.collect(Collectors.toList());
	}

	@Override
	public List<Customer> getInactiveCustomers() {
		List<CustomerDao> customerDaoList = customerRepository.findByIsActiveFalse();
		return customerDaoList.stream().map(customerDao -> customerDaoToDtoMapper.convert(customerDao))
				.collect(Collectors.toList());
	}

	@Override
	public List<MemberShipDuration> getAllDuration() {
		return memberShipDurationRepository.findAll().stream()
				.map(memberShipDurationDao -> memberShipDaoToDtoMapper.convert(memberShipDurationDao))
				.collect(Collectors.toList());

	}

	@Override
	public List<Customer> getCustomersPendingPayment() {
		// Fetch all customers
		List<CustomerDao> activeCustomers = customerRepository.findByIsActiveTrue();

		// Sort payments by date for each customer
		activeCustomers.forEach(customer -> {
			List<IndividualPaymentDao> sortedPayments = customer.getPayments().stream()
					.sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate())) // Sort payments by date (most recent
																				// first)
					.collect(Collectors.toList());
			customer.setPayments(sortedPayments); // Set sorted payments
		});

		List<CustomerDao> finalActiveCustomers = activeCustomers.stream().filter(customer -> {
			// Get the most recent payment
			IndividualPaymentDao mostRecentPayment = customer.getPayments().stream().findFirst() // Get the first
																									// element
																									// (most recent due
																									// to
																									// sorting)
					.orElse(null);

			// Check if the most recent payment exists and if its membership duration
			// matches the provided ID
			return mostRecentPayment != null && (isPaymentPending(mostRecentPayment.getPendingAmount()));
		}).collect(Collectors.toList());

		return finalActiveCustomers.stream().map(customerDao -> customerDaoToDtoMapper.convert(customerDao))
				.collect(Collectors.toList());

	}

	private boolean isPaymentPending(String pendingAmount) {
		if (pendingAmount == null || pendingAmount.trim().isEmpty()) {
			return false; // No pending payment if null or empty
		}

		try {
			// Parse the pending amount as a double
			double amount = Double.parseDouble(pendingAmount);
			// Check if the amount is greater than zero (indicating pending payment)
			return amount > 0;
		} catch (NumberFormatException e) {
			// Handle cases where pendingAmount is not a valid number
			return false;
		}
	}
}
