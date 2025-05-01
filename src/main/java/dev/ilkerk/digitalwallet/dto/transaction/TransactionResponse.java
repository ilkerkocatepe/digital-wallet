package dev.ilkerk.digitalwallet.dto.transaction;

import dev.ilkerk.digitalwallet.domain.entity.OppositePartyType;
import dev.ilkerk.digitalwallet.domain.entity.TransactionStatus;
import dev.ilkerk.digitalwallet.domain.entity.TransactionType;

import java.math.BigDecimal;

public record TransactionResponse(
        Long id,
        TransactionType type,
        OppositePartyType oppositePartyType,
        String oppositeParty,
        BigDecimal amount,
        TransactionStatus status
) {
}
