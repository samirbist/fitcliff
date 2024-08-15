package com.gym.fitcliff.service;

import java.util.List;

import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.SearchCustomer;

public interface CustomerMgmtService {

	Customer saveCustomer(Customer customer);

	Customer getCustomer(Long id);

	List<Customer> getCustomers();

	Customer updateCustomer(Customer customer);

	List<Customer> searchCustomers(SearchCustomer searchCustomer);


}
