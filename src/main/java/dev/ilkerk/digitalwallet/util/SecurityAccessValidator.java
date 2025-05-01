package dev.ilkerk.digitalwallet.util;

import dev.ilkerk.digitalwallet.application.service.CustomerService;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityAccessValidator {
    private final CustomerService customerService;

    public void validateAccess(Long resourceCustomerId) {
        if (SecurityUtil.hasRole("EMPLOYEE")) {
            return;
        }

        String tckn = SecurityUtil.getCurrentUsername();

        Customer authenticatedCustomer = customerService.getByTckn(tckn)
                .orElseThrow(() -> new RuntimeException("Authenticated customer not found"));

        if (!authenticatedCustomer.getId().equals(resourceCustomerId)) {
            throw new AccessDeniedException("Customers can only access their own data");
        }
    }
}
