package com.sovan.customer.service;

import com.sovan.customer.model.Customer;
import java.util.List;
import java.util.UUID;

public interface CustomerService {

    public Customer addCustomer(Customer customer);

    public List<Customer> getAllCustomers();

    public Customer getCustomerByUUID(UUID uuid);

    public boolean updateCustomer(UUID uuid, Customer customer);

    public boolean deleteCustomer(UUID uuid);

    public boolean isValidCustomerId(UUID uuid);

    public boolean updateCustomerByPatch(UUID uuid, Customer customer);

}
