package com.gym.fitcliff.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.fitcliff.api.AdminApi;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.service.CustomerMgmtService;

@RestController
@RequestMapping(value = "/admin")
public class CustomerMgmtController implements AdminApi {

	@Autowired
	private CustomerMgmtService customerMgmtService;

	@Override
	@PostMapping(path = "/customer")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		Customer savedCustomer = customerMgmtService.saveCustomer(customer);
		return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
	}
	
	@Override
	@PutMapping(path = "/customer")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
		Customer savedCustomer = customerMgmtService.updateCustomer(customer);
		return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
	}


	@Override
	@GetMapping(path = "/customer/{id}")
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id) {
		final Customer customer = customerMgmtService.getCustomer(id);
		if (customer != null) {
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@GetMapping(path = "/customer")
	public ResponseEntity<List<Customer>> getCustomers() {
		final List<Customer> customers = customerMgmtService.getCustomers();
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}
}
