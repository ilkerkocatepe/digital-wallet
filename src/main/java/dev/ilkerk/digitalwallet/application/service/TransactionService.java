package dev.ilkerk.digitalwallet.application.service;

import dev.ilkerk.digitalwallet.domain.entity.OppositePartyType;
import dev.ilkerk.digitalwallet.domain.entity.Transaction;
import dev.ilkerk.digitalwallet.domain.entity.TransactionStatus;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Transaction deposit(Wallet wallet, BigDecimal amount, OppositePartyType oppositePartyType, String oppositeParty);
    Transaction withdraw(Wallet wallet, BigDecimal amount, OppositePartyType oppositePartyType, String oppositeParty);
    Transaction approveOrDeny(Long transactionId, TransactionStatus status);
    List<Transaction> getByWallet(Wallet wallet);
}
