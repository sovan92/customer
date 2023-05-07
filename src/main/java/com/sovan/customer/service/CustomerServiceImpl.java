package com.sovan.customer.service;

import com.sovan.customer.model.Address;
import com.sovan.customer.model.Customer;
import com.sovan.customer.model.EnvironmentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 *
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();
        Address address1 = Address.builder()
                .addressLine1("Alpha Address1 ")
                .addressLine2("Alpha Address2")
                .city("Santa Clara")
                .state("California")
                .zipcode("95151")
                .build();

        Address address2 = Address.builder()
                .addressLine1("Beta Address1 ")
                .addressLine2("Beta Address 2")
                .city("Santa Clara")
                .state("California")
                .zipcode("92524")
                .build();
        Customer customer1 = Customer.builder()
                .customerName("XYX")
                .customerType(EnvironmentType.DEV)
                .uuid(UUID.randomUUID())
                .address(address1)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerName("POP")
                .customerType(EnvironmentType.QA)
                .uuid(UUID.randomUUID())
                .address(address2)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getUuid(), customer1);
        customerMap.put(customer2.getUuid(), customer2);
    }


    @Override
    public Customer addCustomer(Customer customer) {

       Customer savedCustomer = Customer.builder()
               .customerName(customer.getCustomerName())
               .customerType(customer.getCustomerType())
               .address(customer.getAddress())
               .uuid(UUID.randomUUID())
               .createdDate(LocalDateTime.now())
               .updatedDate(LocalDateTime.now())
               .build();

       customerMap.put(savedCustomer.getUuid(),savedCustomer);
       return savedCustomer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return new ArrayList<Customer>(customerMap.values());
    }

    @Override
    public Customer getCustomerByUUID(UUID uuid) {
        log.debug("In Service - Customer get customer by UUID");
        if(customerMap.containsKey(uuid)){
            return customerMap.get(uuid);
        }
        else{
            return null;
        }
    }



    @Override
    public boolean updateCustomer(UUID uuid, Customer customer) {
        if(customerMap.containsKey(uuid)){

            Customer customer1 = customerMap.get(uuid);
            customer1.setCustomerName(customer.getCustomerName());
            customer1.setCustomerType(customer.getCustomerType());
            customer1.setAddress(customer.getAddress());
            customer1.setUpdatedDate(LocalDateTime.now());
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean updateCustomerByPatch(UUID uuid, Customer customer) {
        if(customerMap.containsKey(uuid)){

            Customer customer1 = customerMap.get(uuid);
            customer1.setCustomerName(customer.getCustomerName());
            customer1.setCustomerType(customer.getCustomerType());
            customer1.setAddress(customer.getAddress());
            customer1.setUpdatedDate(LocalDateTime.now());
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(UUID uuid) {
        if(customerMap.containsKey(uuid)){
            customerMap.remove(uuid);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean isValidCustomerId(UUID uuid) {
        return customerMap.containsKey(uuid);
    }
}
