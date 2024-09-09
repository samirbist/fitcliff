package com.gym.fitcliff.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerStatusUpdateService {

	@Autowired
	private CustomerRepository customerRepository;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *") // Runs at 12:00 AM (midnight) every day
	// @Scheduled(cron = "0 * * * * *") //runs every minute
	public void updateCustomerActiveFalse() {
		LocalDate today = LocalDate.now();
		log.info("Strarting cron job updateCustomerActiveFalse for date {} ", today);
		// Fetch all customers whose lastDate has passed and are still active
		List<CustomerDao> customersToUpdate = customerRepository.findByLastDateBeforeAndIsActiveTrue(today);

		// Set isActive to false for those customers
		for (CustomerDao customer : customersToUpdate) {
			log.info("Setting custtomer active {}", customersToUpdate);
			customer.setActive(false); // set isActive to false
		}

		// Save the updated customers
		customerRepository.saveAll(customersToUpdate);
		log.info("Ending cron job updateCustomerActiveFalse for date {} ", today);
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *") // Runs at 12:00 AM (midnight) every day
	// @Scheduled(cron = "0 * * * * *") //runs every minute
	public void updateCustomerActiveTrue() {
		LocalDate today = LocalDate.now();
		log.info("Strarting cron job updateCustomerActiveTrue for date {} ", today);
		// Fetch all customers whose lastDate has passed and are still active
		List<CustomerDao> customersToUpdate = customerRepository.findByLastDateGreaterThanEqualAndIsActiveFalse(today);

		// Set isActive to false for those customers
		for (CustomerDao customer : customersToUpdate) {
			log.info("Setting custtomer inactive {}", customersToUpdate);
			customer.setActive(true); // set isActive to false
		}

		// Save the updated customers
		customerRepository.saveAll(customersToUpdate);
		log.info("Ending cron job updateCustomerActiveTrue for date {} ", today);
	}
}
