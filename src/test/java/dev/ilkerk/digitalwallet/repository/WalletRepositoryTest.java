package dev.ilkerk.digitalwallet.repository;

import dev.ilkerk.digitalwallet.adapter.persistence.CustomerRepository;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.domain.entity.Role;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import dev.ilkerk.digitalwallet.domain.entity.Currency;
import dev.ilkerk.digitalwallet.adapter.persistence.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldSaveAndFindWalletByCustomer() {
        Customer customer = Customer.builder().name("John").surname("Doe").tckn("123456").role(Role.CUSTOMER).build();

        customer = customerRepository.save(customer);

        Wallet wallet = Wallet.builder()
                .customer(customer)
                .walletName("Main Wallet")
                .currency(Currency.TRY)
                .balance(BigDecimal.ZERO)
                .usableBalance(BigDecimal.ZERO)
                .build();

        walletRepository.save(wallet);

        List<Wallet> wallets = walletRepository.findByCustomer(customer);
        assertThat(wallets).hasSize(1);
    }
}
