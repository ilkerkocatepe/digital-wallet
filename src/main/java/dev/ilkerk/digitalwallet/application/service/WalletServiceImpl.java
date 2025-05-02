package dev.ilkerk.digitalwallet.application.service;

import dev.ilkerk.digitalwallet.adapter.persistence.WalletRepository;
import dev.ilkerk.digitalwallet.domain.entity.Currency;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import dev.ilkerk.digitalwallet.util.SecurityAccessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final SecurityAccessValidator securityAccessValidator;

    @Override
    public Wallet createWallet(Customer customer, String walletName, Currency currency,
                               boolean activeForShopping, boolean activeForWithdraw) {
        securityAccessValidator.validateAccess(customer.getId());

        Wallet wallet = Wallet.builder()
                .customer(customer)
                .walletName(walletName)
                .currency(currency)
                .activeForShopping(activeForShopping)
                .activeForWithdraw(activeForWithdraw)
                .balance(BigDecimal.ZERO)
                .usableBalance(BigDecimal.ZERO)
                .build();

        return walletRepository.save(wallet);
    }

    @Override
    public List<Wallet> getWalletsByCustomer(Customer customer) {
        securityAccessValidator.validateAccess(customer.getId());

        return walletRepository.findByCustomer(customer);
    }

    @Override
    public List<Wallet> getWalletsByCustomerAndCurrency(Customer customer, Currency currency) {
        securityAccessValidator.validateAccess(customer.getId());

        return walletRepository.findByCustomerAndCurrency(customer, currency);
    }

    @Override
    public Wallet getWalletById(Long walletId) {
        return walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Override
    public Wallet updateWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}
