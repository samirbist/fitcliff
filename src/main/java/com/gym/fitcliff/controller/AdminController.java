package com.gym.fitcliff.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.gym.fitcliff.model.DocumentImage;
import com.gym.fitcliff.model.Group;
import com.gym.fitcliff.model.SearchCustomer;
import com.gym.fitcliff.service.CustomerMgmtService;
import com.gym.fitcliff.service.DocumentMgmtService;
import com.gym.fitcliff.service.GroupMgmtService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/admin")
public class AdminController implements AdminApi {

	@Autowired
	private CustomerMgmtService customerMgmtService;

	@Autowired
	private GroupMgmtService groupMgmtService;

	@Autowired
	private DocumentMgmtService documentMgmtService;

	@Autowired
	private HttpServletResponse response;

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

	@Override
	@PostMapping("/customer/search")
	public ResponseEntity<List<Customer>> searchCustomer(@RequestBody SearchCustomer searchCustomer) {
		final List<Customer> customers = customerMgmtService.searchCustomers(searchCustomer);
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@Override
	@PostMapping("/group")
	public ResponseEntity<Group> createGroup(@RequestBody Group group) {
		return new ResponseEntity<>(groupMgmtService.createGroup(group), HttpStatus.CREATED);
	}

	@Override
	@GetMapping("/group")
	public ResponseEntity<List<Group>> getGroups() {
		return new ResponseEntity<>(groupMgmtService.getGroup(), HttpStatus.OK);
	}

	@Override
	@PostMapping("/documentId")
	public ResponseEntity<DocumentImage> createDocumentId(
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		return new ResponseEntity<>(documentMgmtService.createIdDocument(fileName, image), HttpStatus.CREATED);
	}

	@Override
	@GetMapping("/documentId/{id}")
	public ResponseEntity<Void> getDocumentId(@PathVariable("id") Long id) {
		try {
			MongoDataDto mongoData = documentMgmtService.getIdDocument(id);
			FileCopyUtils.copy(mongoData.getStream(), response.getOutputStream());
		} catch (Exception e) {
			log.error("Error in getting template data for id {} : {} ", id, e);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}

	@Override
	@DeleteMapping("/admin/documentId/{id}")
	public ResponseEntity<Void> deleteDocumentId(@PathVariable("id") Long id) {
		documentMgmtService.deleteIdDocument(id);
		return ResponseEntity.ok().build();
	}
}
