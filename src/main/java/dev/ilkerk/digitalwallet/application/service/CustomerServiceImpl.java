package dev.ilkerk.digitalwallet.application.service;

import dev.ilkerk.digitalwallet.adapter.persistence.CustomerRepository;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Optional<Customer> getByTckn(String tckn) {
        return customerRepository.findByTckn(tckn);
    }
}
