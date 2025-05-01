package dev.ilkerk.digitalwallet.application.service;

import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.domain.entity.Currency;

import java.util.List;

public interface WalletService {
    Wallet createWallet(Customer customer, String walletName, Currency currency,
                        boolean activeForShopping, boolean activeForWithdraw);
    List<Wallet> getWalletsByCustomer(Customer customer);
    List<Wallet> getWalletsByCustomerAndCurrency(Customer customer, Currency currency);
    Wallet getWalletById(Long walletId);
    Wallet updateWallet(Wallet wallet);
}

