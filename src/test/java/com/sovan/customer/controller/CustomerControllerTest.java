package com.sovan.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sovan.customer.model.Address;
import com.sovan.customer.model.Customer;
import com.sovan.customer.model.EnvironmentType;
import com.sovan.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;
    // Auto wired by spring framework

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService service;
    // It adds a mocked customer service

    List<Customer> customerList;

    @BeforeEach
    public void setup() {
        Customer abc = Customer.builder()
                .customerName("ABC")
                .customerType(EnvironmentType.PRDUCTION)
                .address(Address.builder().addressLine1("3655 Pruneridge Ave")
                        .addressLine2("Apt 164")
                        .state("CA")
                        .city("Santa Clara")
                        .zipcode("95051").build())
                .uuid(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        Customer pqr = Customer.builder()
                .customerName("PQR")
                .customerType(EnvironmentType.PRDUCTION)
                .address(Address.builder().addressLine1("3655 Pruneridge Ave")
                        .addressLine2("Apt 164")
                        .state("CA")
                        .city("Santa Clara")
                        .zipcode("95051").build())
                .uuid(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        customerList = List.of(abc, pqr);
    }


    @Test
    void testListCustomers() throws Exception {

        given(service.getAllCustomers()).willReturn(customerList);
        mockMvc.perform(get("/api/v1/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));


    }

    @Test
    void getCustomerById() throws Exception {

        Customer customer = customerList.get(0);
        given(service.getCustomerByUUID(any(UUID.class))).willReturn(customer);
        mockMvc.perform(get("/api/v1/customers/" + customer.getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid", is(customer.getUuid().toString())))
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));

    }

    @Test
    void updateCustomer() throws Exception {
        Customer customer = customerList.get(0);

        mockMvc.perform(put("/api/v1/customers/"+ customer.getUuid().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isResetContent())
                .andExpect(header().string("isCustomerUpdated", String.valueOf(Boolean.valueOf(false))));

        verify(service).updateCustomer(customer.getUuid(), customer);
    }

    @Test
    void handlePost() throws Exception {
        UUID uuid = UUID.randomUUID();
        Customer mno = Customer.builder()
                .customerName("OWL")
                .customerType(EnvironmentType.PRDUCTION)
                .address(Address.builder().addressLine1("Abc road")
                        .addressLine2("Apt 164")
                        .state("CA")
                        .city("Santa Clara")
                        .zipcode("95051").build())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .uuid(uuid)
                .build();
        given(service.addCustomer(any(Customer.class))).willReturn(mno);
        mockMvc.perform(post("/api/v1/customers")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mno)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/customers/" + uuid.toString()));
    }

    @Test
    void deleteById() throws Exception{
        Customer customer = customerList.get(0);
        mockMvc.perform(delete("/api/v1/customers/"+customer.getUuid().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string("isCustomerDeleted", String.valueOf(Boolean.valueOf(false))));
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(service).deleteCustomer(uuidArgumentCaptor.capture());
        assertThat(customer.getUuid()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void updateCustomerByPatch() throws Exception {
        Customer customer = customerList.get(0);
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "STU");
        mockMvc.perform(patch("/api/v1/customers/"+ customer.getUuid().toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isResetContent())
                .andExpect(header().string("isCustomerUpdated", String.valueOf(Boolean.valueOf(false))));


        verify(service).updateCustomer(customer.getUuid(), Customer.builder().customerName("STU").build());

    }
}