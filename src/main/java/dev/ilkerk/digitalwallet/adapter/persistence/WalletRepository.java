package dev.ilkerk.digitalwallet.adapter.persistence;

import dev.ilkerk.digitalwallet.domain.entity.Currency;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {
    List<Wallet> findByCustomer(Customer customer);
    List<Wallet> findByCustomerAndCurrency(Customer customer, Currency currency);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    Optional<Wallet> findByIdForUpdate(@Param("id") Long id);
}
