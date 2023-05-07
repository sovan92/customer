package com.sovan.customer.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Builder
@Data
public class Customer {

    private UUID uuid;
    private String customerName;
    private Address address;
    private EnvironmentType customerType;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
