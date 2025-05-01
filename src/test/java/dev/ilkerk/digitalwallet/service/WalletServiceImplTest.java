package dev.ilkerk.digitalwallet.service;

import dev.ilkerk.digitalwallet.adapter.persistence.WalletRepository;
import dev.ilkerk.digitalwallet.application.service.WalletServiceImpl;
import dev.ilkerk.digitalwallet.domain.entity.Currency;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.domain.entity.Wallet;
import dev.ilkerk.digitalwallet.util.SecurityAccessValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private SecurityAccessValidator securityAccessValidator;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Customer customer;
    private Wallet wallet1;
    private Wallet wallet2;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .build();

        wallet1 = Wallet.builder()
                .id(1L)
                .customer(customer)
                .walletName("Test Wallet 1")
                .currency(Currency.TRY)
                .activeForShopping(true)
                .activeForWithdraw(true)
                .balance(BigDecimal.valueOf(1000))
                .usableBalance(BigDecimal.valueOf(1000))
                .build();

        wallet2 = Wallet.builder()
                .id(2L)
                .customer(customer)
                .walletName("Test Wallet 2")
                .currency(Currency.USD)
                .activeForShopping(false)
                .activeForWithdraw(true)
                .balance(BigDecimal.valueOf(500))
                .usableBalance(BigDecimal.valueOf(500))
                .build();
    }

    @Test
    void createWallet_shouldCreateAndReturnNewWallet() {
        // Arrange
        String walletName = "New Test Wallet";
        Currency currency = Currency.EUR;
        boolean activeForShopping = true;
        boolean activeForWithdraw = false;

        Wallet expectedWallet = Wallet.builder()
                .customer(customer)
                .walletName(walletName)
                .currency(currency)
                .activeForShopping(activeForShopping)
                .activeForWithdraw(activeForWithdraw)
                .balance(BigDecimal.ZERO)
                .usableBalance(BigDecimal.ZERO)
                .build();

        when(walletRepository.save(any(Wallet.class))).thenReturn(expectedWallet);
        doNothing().when(securityAccessValidator).validateAccess(anyLong());

        // Act
        Wallet result = walletService.createWallet(customer, walletName, currency, activeForShopping, activeForWithdraw);

        // Assert
        assertNotNull(result);
        assertEquals(expectedWallet, result);
        assertEquals(walletName, result.getWalletName());
        assertEquals(currency, result.getCurrency());
        assertEquals(activeForShopping, result.isActiveForShopping());
        assertEquals(activeForWithdraw, result.isActiveForWithdraw());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals(BigDecimal.ZERO, result.getUsableBalance());

        verify(securityAccessValidator, times(1)).validateAccess(customer.getId());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void getWalletsByCustomer_shouldReturnAllCustomerWallets() {
        // Arrange
        List<Wallet> expectedWallets = Arrays.asList(wallet1, wallet2);
        when(walletRepository.findByCustomer(customer)).thenReturn(expectedWallets);
        doNothing().when(securityAccessValidator).validateAccess(anyLong());

        // Act
        List<Wallet> result = walletService.getWalletsByCustomer(customer);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedWallets, result);
        assertTrue(result.contains(wallet1));
        assertTrue(result.contains(wallet2));

        verify(securityAccessValidator, times(1)).validateAccess(customer.getId());
        verify(walletRepository, times(1)).findByCustomer(customer);
    }

    @Test
    void getWalletsByCustomerAndCurrency_shouldReturnFilteredWallets() {
        // Arrange
        List<Wallet> expectedWallets = Collections.singletonList(wallet1);
        when(walletRepository.findByCustomerAndCurrency(customer, Currency.TRY)).thenReturn(expectedWallets);
        doNothing().when(securityAccessValidator).validateAccess(anyLong());

        // Act
        List<Wallet> result = walletService.getWalletsByCustomerAndCurrency(customer, Currency.TRY);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedWallets, result);
        assertTrue(result.contains(wallet1));
        assertFalse(result.contains(wallet2));

        verify(securityAccessValidator, times(1)).validateAccess(customer.getId());
        verify(walletRepository, times(1)).findByCustomerAndCurrency(customer, Currency.TRY);
    }

    @Test
    void getWalletById_shouldReturnWalletWhenExists() {
        // Arrange
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet1));

        // Act
        Wallet result = walletService.getWalletById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(wallet1, result);
        assertEquals(1L, result.getId());

        verify(walletRepository, times(1)).findById(1L);
    }

    @Test
    void getWalletById_shouldThrowException_whenWalletNotFound() {
        // Arrange
        when(walletRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> walletService.getWalletById(999L));

        assertEquals("Wallet not found", exception.getMessage());
        verify(walletRepository, times(1)).findById(999L);
    }

    @Test
    void updateWallet_shouldUpdateAndReturnWallet() {
        // Arrange
        Wallet walletToUpdate = wallet1;
        walletToUpdate.setWalletName("Updated Wallet Name");
        walletToUpdate.setActiveForShopping(false);

        when(walletRepository.save(walletToUpdate)).thenReturn(walletToUpdate);

        // Act
        Wallet result = walletService.updateWallet(walletToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals(walletToUpdate, result);
        assertEquals("Updated Wallet Name", result.getWalletName());
        assertFalse(result.isActiveForShopping());

        verify(walletRepository, times(1)).save(walletToUpdate);
    }
}
