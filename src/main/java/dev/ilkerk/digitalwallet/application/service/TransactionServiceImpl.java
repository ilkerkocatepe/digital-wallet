package dev.ilkerk.digitalwallet.application.service;

import dev.ilkerk.digitalwallet.adapter.persistence.TransactionRepository;
import dev.ilkerk.digitalwallet.domain.entity.OppositePartyType;
import dev.ilkerk.digitalwallet.domain.entity.Transaction;
import dev.ilkerk.digitalwallet.domain.entity.TransactionStatus;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import dev.ilkerk.digitalwallet.domain.entity.TransactionType;
import dev.ilkerk.digitalwallet.util.SecurityAccessValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private static final int AUTO_APPROVE_LIMIT = 1000;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final SecurityAccessValidator securityAccessValidator;

    @Override
    @Transactional
    public Transaction deposit(Wallet wallet, BigDecimal amount, OppositePartyType oppositePartyType, String oppositeParty) {
        securityAccessValidator.validateAccess(wallet.getCustomer().getId());

        TransactionStatus status = amount.compareTo(BigDecimal.valueOf(AUTO_APPROVE_LIMIT)) > 0
                ? TransactionStatus.PENDING
                : TransactionStatus.APPROVED;

        if (status == TransactionStatus.APPROVED) {
            wallet.setBalance(wallet.getBalance().add(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
        } else {
            wallet.setBalance(wallet.getBalance().add(amount));
        }

        wallet = walletService.updateWallet(wallet);

        Transaction tx = Transaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(oppositePartyType)
                .oppositeParty(oppositeParty)
                .status(status)
                .build();

        tx = transactionRepository.save(tx);

        return tx;
    }

    @Override
    @Transactional
    public Transaction withdraw(Wallet wallet, BigDecimal amount, OppositePartyType oppositePartyType, String oppositeParty) {
        securityAccessValidator.validateAccess(wallet.getCustomer().getId());

        if (oppositePartyType.equals(OppositePartyType.IBAN) && !wallet.isActiveForWithdraw()) {
            throw new RuntimeException("Wallet is not active for withdraw");
        }

        if (oppositePartyType.equals(OppositePartyType.PAYMENT) && !wallet.isActiveForShopping()) {
            throw new RuntimeException("Wallet is not active for shopping");
        }

        TransactionStatus status = amount.compareTo(BigDecimal.valueOf(AUTO_APPROVE_LIMIT)) > 0
                ? TransactionStatus.PENDING
                : TransactionStatus.APPROVED;

        if (status == TransactionStatus.APPROVED) {
            wallet.setBalance(wallet.getBalance().subtract(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        } else {
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        }

        wallet = walletService.updateWallet(wallet);

        Transaction tx = Transaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .oppositePartyType(oppositePartyType)
                .oppositeParty(oppositeParty)
                .status(status)
                .build();

        return transactionRepository.save(tx);
    }

    @Override
    @Transactional
    public Transaction approveOrDeny(Long transactionId, TransactionStatus status) {
        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (tx.getStatus() != TransactionStatus.PENDING) {
            throw new RuntimeException("Transaction is already processed");
        }

        Wallet wallet = tx.getWallet();

        if (status == TransactionStatus.APPROVED) {
            if (tx.getType() == TransactionType.WITHDRAW) {
                wallet.setBalance(wallet.getBalance().subtract(tx.getAmount()));
            } else if (tx.getType() == TransactionType.DEPOSIT) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(tx.getAmount()));
            }
        } else {
            if (tx.getType() == TransactionType.WITHDRAW) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(tx.getAmount()));
            } else if (tx.getType() == TransactionType.DEPOSIT) {
                wallet.setBalance(wallet.getBalance().subtract(tx.getAmount()));
            }
        }

        tx.setStatus(status);
        return transactionRepository.save(tx);
    }

    @Override
    public List<Transaction> getByWallet(Wallet wallet) {
        securityAccessValidator.validateAccess(wallet.getCustomer().getId());

        return transactionRepository.findByWallet(wallet);
    }
}
