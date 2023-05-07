package com.sovan.customer.controller;

import com.sovan.customer.model.Customer;
import com.sovan.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
/*
   Rest controller applies to reponse Body annotation converts the response body to JSON .
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;


    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * @param uuid
     * @return
     */
    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID uuid) {
        log.debug("Get Customer by ID executed");
        return customerService.getCustomerByUUID(uuid);
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") UUID uuid, @RequestBody Customer customer) {
        log.debug("custoomer uuid" + uuid);
        boolean isCustomerUpdated = customerService.updateCustomer(uuid, customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("isCustomerUpdated", Boolean.valueOf(isCustomerUpdated).toString());
        return new ResponseEntity<Customer>(headers, HttpStatus.RESET_CONTENT);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Customer> handlePost(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.addCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customers/" + savedCustomer.getUuid());
        return new ResponseEntity<Customer>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.DELETE)
    public ResponseEntity<Customer> deleteById(@PathVariable("customerId") UUID uuid) {

        boolean isCustomerDeleted = customerService.deleteCustomer(uuid);
        HttpHeaders headers = new HttpHeaders();
        headers.add("isCustomerDeleted", Boolean.valueOf(isCustomerDeleted).toString());
        return new ResponseEntity<Customer>(headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.PATCH)
    public ResponseEntity<Customer> updateCustomerByPatch(@PathVariable("customerId") UUID uuid, @RequestBody Customer customer) {
        log.debug("custoomer uuid" + uuid);
        boolean isCustomerUpdated = customerService.updateCustomer(uuid, customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("isCustomerUpdated", Boolean.valueOf(isCustomerUpdated).toString());
        return new ResponseEntity<Customer>(headers, HttpStatus.RESET_CONTENT);
    }


}
