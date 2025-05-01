package dev.ilkerk.digitalwallet.dto.customer;

public record CustomerResponse(
        Long id,
        String name,
        String surname,
        String tckn
) {
}
