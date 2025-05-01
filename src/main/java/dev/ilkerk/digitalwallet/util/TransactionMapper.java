package dev.ilkerk.digitalwallet.util;

import dev.ilkerk.digitalwallet.domain.entity.Transaction;
import dev.ilkerk.digitalwallet.dto.transaction.TransactionResponse;

public final class TransactionMapper {
    private TransactionMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a Transaction entity to a TransactionResponse DTO.
     *
     * @param tx the Transaction entity to convert
     * @return the converted TransactionResponse DTO
     */
    public static TransactionResponse toResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getId(),
                tx.getType(),
                tx.getOppositePartyType(),
                tx.getOppositeParty(),
                tx.getAmount(),
                tx.getStatus()
        );
    }
}
