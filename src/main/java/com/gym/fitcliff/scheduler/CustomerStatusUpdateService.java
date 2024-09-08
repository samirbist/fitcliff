package com.gym.fitcliff.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.fitcliff.entity.CustomerDao;
import com.gym.fitcliff.repository.CustomerRepository;

@Service
public class CustomerStatusUpdateService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // Runs at 12:00 AM (midnight) every day
    //@Scheduled(cron = "0 * * * * *") //runs every minute
    public void updateCustomerStatus() {
        LocalDate today = LocalDate.now();
        
        // Fetch all customers whose lastDate has passed and are still active
        List<CustomerDao> customersToUpdate = customerRepository.findByLastDateBeforeAndIsActiveTrue(today);

        // Set isActive to false for those customers
        for (CustomerDao customer : customersToUpdate) {
            customer.setActive(false);  // set isActive to false
        }

        // Save the updated customers
        customerRepository.saveAll(customersToUpdate);
    }
}
