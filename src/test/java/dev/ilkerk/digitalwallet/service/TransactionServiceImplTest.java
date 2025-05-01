package dev.ilkerk.digitalwallet.service;

import dev.ilkerk.digitalwallet.adapter.persistence.TransactionRepository;
import dev.ilkerk.digitalwallet.application.service.TransactionServiceImpl;
import dev.ilkerk.digitalwallet.application.service.WalletService;
import dev.ilkerk.digitalwallet.domain.entity.Currency;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.domain.entity.OppositePartyType;
import dev.ilkerk.digitalwallet.domain.entity.Transaction;
import dev.ilkerk.digitalwallet.domain.entity.TransactionStatus;
import dev.ilkerk.digitalwallet.domain.entity.TransactionType;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import dev.ilkerk.digitalwallet.util.SecurityAccessValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private SecurityAccessValidator securityAccessValidator;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Customer customer = Customer.builder()
                .id(1L)
                .build();

        wallet = Wallet.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(1000))
                .usableBalance(BigDecimal.valueOf(1000))
                .activeForWithdraw(true)
                .activeForShopping(true)
                .currency(Currency.TRY)
                .customer(customer)
                .build();

        // Security validator mock setup
        doNothing().when(securityAccessValidator).validateAccess(any(Long.class));

        // Setup WalletService mock to return the wallet when updateWallet is called
        when(walletService.updateWallet(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void deposit_shouldReturnApprovedTransaction_whenAmountIsLessThan1000() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(500);

        Transaction expectedTransaction = Transaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(OppositePartyType.IBAN)
                .oppositeParty("TR123")
                .status(TransactionStatus.APPROVED)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);

        // Act
        Transaction tx = transactionService.deposit(wallet, amount, OppositePartyType.IBAN, "TR123");

        // Assert
        assertEquals(expectedTransaction, tx);
        assertEquals(TransactionStatus.APPROVED, tx.getStatus());
        assertEquals(BigDecimal.valueOf(1500), wallet.getBalance());
        assertEquals(BigDecimal.valueOf(1500), wallet.getUsableBalance());
    }

    @Test
    void deposit_shouldReturnPendingTransaction_whenAmountIsGreaterThan1000() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(2000);

        Transaction expectedTransaction = Transaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(OppositePartyType.IBAN)
                .oppositeParty("TR123")
                .status(TransactionStatus.PENDING)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);

        // Act
        Transaction tx = transactionService.deposit(wallet, amount, OppositePartyType.IBAN, "TR123");

        // Assert
        assertEquals(expectedTransaction, tx);
        assertEquals(TransactionStatus.PENDING, tx.getStatus());
        assertEquals(BigDecimal.valueOf(3000), wallet.getBalance());
        assertEquals(BigDecimal.valueOf(1000), wallet.getUsableBalance());
    }

    @Test
    void withdraw_shouldReturnApprovedTransaction_whenAmountIsLessThan1000() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(500);

        Transaction expectedTransaction = Transaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .oppositePartyType(OppositePartyType.IBAN)
                .oppositeParty("TR123")
                .status(TransactionStatus.APPROVED)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);

        // Act
        Transaction tx = transactionService.withdraw(wallet, amount, OppositePartyType.IBAN, "TR123");

        // Assert
        assertEquals(expectedTransaction, tx);
        assertEquals(TransactionStatus.APPROVED, tx.getStatus());
        assertEquals(BigDecimal.valueOf(500), wallet.getBalance());
        assertEquals(BigDecimal.valueOf(500), wallet.getUsableBalance());
    }

    @Test
    void withdraw_shouldReturnPendingTransaction_whenAmountIsGreaterThan1000() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(2000);

        Transaction expectedTransaction = Transaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .oppositePartyType(OppositePartyType.IBAN)
                .oppositeParty("TR123")
                .status(TransactionStatus.PENDING)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);

        // Act
        Transaction tx = transactionService.withdraw(wallet, amount, OppositePartyType.IBAN, "TR123");

        // Assert
        assertEquals(expectedTransaction, tx);
        assertEquals(TransactionStatus.PENDING, tx.getStatus());
        assertEquals(BigDecimal.valueOf(1000), wallet.getBalance());
        assertEquals(BigDecimal.valueOf(-1000), wallet.getUsableBalance());
    }
}
