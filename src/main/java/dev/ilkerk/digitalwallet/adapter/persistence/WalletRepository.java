package dev.ilkerk.digitalwallet.adapter.persistence;

import dev.ilkerk.digitalwallet.domain.entity.Currency;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {
    List<Wallet> findByCustomer(Customer customer);
    List<Wallet> findByCustomerAndCurrency(Customer customer, Currency currency);
}
