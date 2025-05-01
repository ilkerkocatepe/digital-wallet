package dev.ilkerk.digitalwallet.adapter.persistence;

import dev.ilkerk.digitalwallet.domain.entity.Transaction;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByWallet(Wallet wallet);
}
