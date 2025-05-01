package dev.ilkerk.digitalwallet.dto.transaction;

import dev.ilkerk.digitalwallet.domain.entity.TransactionStatus;
import jakarta.validation.constraints.NotNull;

public record ApproveTransactionRequest(
        @NotNull Long transactionId,
        @NotNull TransactionStatus status
) {
}
