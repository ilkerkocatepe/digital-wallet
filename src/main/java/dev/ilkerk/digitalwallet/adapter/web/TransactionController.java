package dev.ilkerk.digitalwallet.adapter.web;

import dev.ilkerk.digitalwallet.application.service.TransactionService;
import dev.ilkerk.digitalwallet.application.service.WalletService;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import dev.ilkerk.digitalwallet.dto.transaction.TransactionResponse;
import dev.ilkerk.digitalwallet.dto.transaction.DepositRequest;
import dev.ilkerk.digitalwallet.dto.transaction.WithdrawRequest;
import dev.ilkerk.digitalwallet.dto.transaction.ApproveTransactionRequest;
import dev.ilkerk.digitalwallet.util.TransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final WalletService walletService;

    @Operation(
            summary = "Deposit money to a wallet",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PostMapping("/deposit")
    public TransactionResponse deposit(@RequestBody @Valid DepositRequest request) {
        Wallet wallet = walletService.getWalletById(request.walletId());

        return TransactionMapper.toResponse(
                transactionService.deposit(wallet, request.amount(), request.oppositePartyType(), request.oppositeParty())
        );
    }

    @Operation(
            summary = "Withdraw money from a wallet",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PostMapping("/withdraw")
    public TransactionResponse withdraw(@RequestBody @Valid WithdrawRequest request) {
        Wallet wallet = walletService.getWalletById(request.walletId());

        return TransactionMapper.toResponse(
                transactionService.withdraw(wallet, request.amount(), request.oppositePartyType(), request.oppositeParty())
        );
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/approve")
    public TransactionResponse approve(@RequestBody @Valid ApproveTransactionRequest request) {
        return TransactionMapper.toResponse(
                transactionService.approveOrDeny(request.transactionId(), request.status())
        );
    }

    @Operation(
            summary = "List all transactions for a wallet",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @GetMapping("/wallet/{walletId}")
    public List<TransactionResponse> listTransactions(@PathVariable Long walletId) {
        Wallet wallet = walletService.getWalletById(walletId);
        return transactionService.getByWallet(wallet).stream()
                .map(TransactionMapper::toResponse)
                .toList();
    }
}
