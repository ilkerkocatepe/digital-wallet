package dev.ilkerk.digitalwallet.dto.transaction;

import dev.ilkerk.digitalwallet.domain.entity.OppositePartyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequest(
        @NotNull Long walletId,
        @DecimalMin("0.01") BigDecimal amount,
        @NotNull OppositePartyType oppositePartyType,
        @NotBlank String oppositeParty
) {
}
