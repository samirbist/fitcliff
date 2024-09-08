package com.gym.fitcliff.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gym.fitcliff.api.AdminApi;
import com.gym.fitcliff.dto.MongoDataDto;
import com.gym.fitcliff.model.Customer;
import com.gym.fitcliff.model.Image;
import com.gym.fitcliff.model.MemberShipDuration;
import com.gym.fitcliff.service.CustomerMgmtService;
import com.gym.fitcliff.service.ImageMgmtService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/admin")
public class AdminController implements AdminApi {

	@Autowired
	private CustomerMgmtService customerMgmtService;

	@Autowired
	private ImageMgmtService imageMgmtService;

	@Autowired
	private HttpServletResponse response;

	@Override
	@GetMapping(path = "/customer/search/{text}")
	public ResponseEntity<List<Customer>> getCustomerByText(@PathVariable("text") String text) {
		final List<Customer> customers = customerMgmtService.searchCustomersBy(text);
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

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
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
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

	@Override
	@PostMapping("/customer/search")
	public ResponseEntity<List<Customer>> searchCustomerByField(@RequestBody Customer searchCustomer) {
		final List<Customer> customers = customerMgmtService.searchCustomers(searchCustomer);
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@Override
	@PostMapping("/image")
	public ResponseEntity<Image> createImage(@RequestParam(value = "fileName", required = false) String fileName,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		return new ResponseEntity<>(imageMgmtService.createImage(fileName, image), HttpStatus.CREATED);
	}

	@Override
	@GetMapping("/image/{id}")
	public ResponseEntity<Void> getImage(@PathVariable("id") Long id) {
		try {
			MongoDataDto mongoData = imageMgmtService.getImage(id);
			FileCopyUtils.copy(mongoData.getStream(), response.getOutputStream());
		} catch (Exception e) {
			log.error("Error in getting iamge data for id {} : {} ", id, e);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/image/{id}")
	public ResponseEntity<Void> deleteImage(@PathVariable("id") Long id) {
		imageMgmtService.deleteImage(id);
		return ResponseEntity.ok().build();
	}

	@Override
	@GetMapping("/customer/{durationId}/search")
	public ResponseEntity<List<Customer>> getCustomerByDuration(@PathVariable("durationId") Long duration) {
		return new ResponseEntity<>(customerMgmtService.getCustomerByDuration(duration), HttpStatus.OK);
	}

	@Override
	@GetMapping("/customer/inactivesoon")
	public ResponseEntity<List<Customer>> getCustomersInactiveSoon() {
		return new ResponseEntity<>(customerMgmtService.getCustomersInactiveSoon(), HttpStatus.OK);
	}

	@Override
	@GetMapping("/customer/inactive")
	public ResponseEntity<List<Customer>> getInactiveCustomers() {
		return new ResponseEntity<>(customerMgmtService.getInactiveCustomers(), HttpStatus.OK);
	}

	@Override
	@GetMapping("/duration")
	public ResponseEntity<List<MemberShipDuration>> getAllDuration() {
		return new ResponseEntity<>(customerMgmtService.getAllDuration(), HttpStatus.OK);
	}

	@GetMapping("/customer/pendingpayment")
	public ResponseEntity<List<Customer>> getCustomersPendingPayment() {
		return new ResponseEntity<>(customerMgmtService.getCustomersPendingPayment(), HttpStatus.OK);
	}

}
