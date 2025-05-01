package dev.ilkerk.digitalwallet.dto.wallet;

import dev.ilkerk.digitalwallet.domain.entity.Currency;

import java.math.BigDecimal;

public record WalletResponse(
        Long id,
        String walletName,
        Currency currency,
        boolean activeForShopping,
        boolean activeForWithdraw,
        BigDecimal balance,
        BigDecimal usableBalance
) {
}
