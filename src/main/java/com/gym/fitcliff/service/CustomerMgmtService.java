package com.gym.fitcliff.service;

import java.util.List;

import com.gym.fitcliff.model.Customer;

public interface CustomerMgmtService {

	Customer saveCustomer(Customer customer);

	Customer getCustomer(Long id);

	List<Customer> getCustomers();

	Customer updateCustomer(Customer customer);

	List<Customer> searchCustomersBy(String text);

	List<Customer> searchCustomers(Customer searchCustomer);

	List<Customer> getCustomerByDuration(Integer duration);

	List<Customer> getCustomersInactiveSoon();

	List<Customer>  getInactiveCustomers();

}
