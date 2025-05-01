package dev.ilkerk.digitalwallet.util;

import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.dto.customer.CustomerResponse;

public final class CustomerMapper {
    private CustomerMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a Customer entity to a CustomerResponse DTO.
     *
     * @param customer the Customer entity to convert
     * @return the converted CustomerResponse DTO
     */
    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                customer.getTckn()
        );
    }
}
