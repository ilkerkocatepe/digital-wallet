package dev.ilkerk.digitalwallet.util;

import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import dev.ilkerk.digitalwallet.dto.wallet.WalletResponse;

public final class WalletMapper {
    private WalletMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a Wallet entity to a WalletResponse DTO.
     *
     * @param wallet the Wallet entity to convert
     * @return the converted WalletResponse DTO
     */
    public static WalletResponse toResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getWalletName(),
                wallet.getCurrency(),
                wallet.isActiveForShopping(),
                wallet.isActiveForWithdraw(),
                wallet.getBalance(),
                wallet.getUsableBalance()
        );
    }
}
