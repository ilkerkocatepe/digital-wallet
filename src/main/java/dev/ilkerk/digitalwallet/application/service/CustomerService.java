package dev.ilkerk.digitalwallet.application.service;

import dev.ilkerk.digitalwallet.domain.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Customer getById(Long id);
    Optional<Customer> getByTckn(String tckn);
}
