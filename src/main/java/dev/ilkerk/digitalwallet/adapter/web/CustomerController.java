package dev.ilkerk.digitalwallet.adapter.web;

import dev.ilkerk.digitalwallet.application.service.CustomerService;
import dev.ilkerk.digitalwallet.dto.customer.CustomerResponse;
import dev.ilkerk.digitalwallet.util.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public CustomerResponse getCustomerById(@PathVariable Long id) {
        return CustomerMapper.toResponse(customerService.getById(id));
    }
}
