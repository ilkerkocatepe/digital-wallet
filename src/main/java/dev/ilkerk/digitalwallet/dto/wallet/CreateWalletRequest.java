package dev.ilkerk.digitalwallet.dto.wallet;

import dev.ilkerk.digitalwallet.domain.entity.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateWalletRequest(
        @NotBlank String walletName,
        @NotNull Currency currency,
        boolean activeForShopping,
        boolean activeForWithdraw
) {
}
