package dev.ilkerk.digitalwallet.adapter.web;

import dev.ilkerk.digitalwallet.application.service.CustomerService;
import dev.ilkerk.digitalwallet.application.service.WalletService;
import dev.ilkerk.digitalwallet.domain.entity.Currency;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.dto.wallet.CreateWalletRequest;
import dev.ilkerk.digitalwallet.dto.wallet.WalletResponse;
import dev.ilkerk.digitalwallet.util.WalletMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final CustomerService customerService;

    @Operation(
            summary = "Create wallet",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PostMapping("create/{customerId}")
    public WalletResponse createWallet(@PathVariable Long customerId,
                                       @RequestBody @Valid CreateWalletRequest request) {
        Customer customer = customerService.getById(customerId);

        return WalletMapper.toResponse(
                walletService.createWallet(
                        customer,
                        request.walletName(),
                        request.currency(),
                        request.activeForShopping(),
                        request.activeForWithdraw()
                )
        );
    }

    @Operation(
            summary = "Get wallets",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @GetMapping("customer/{customerId}")
    public List<WalletResponse> getWallets(@PathVariable Long customerId,
                                           @RequestParam(required = false) Currency currency) {
        Customer customer = customerService.getById(customerId);

        return (currency == null)
                ? walletService.getWalletsByCustomer(customer).stream().map(WalletMapper::toResponse).toList()
                : walletService.getWalletsByCustomerAndCurrency(customer, currency).stream().map(WalletMapper::toResponse).toList();
    }
}
