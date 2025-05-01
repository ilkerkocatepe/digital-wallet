package dev.ilkerk.digitalwallet.application.service;

import dev.ilkerk.digitalwallet.adapter.persistence.CustomerRepository;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String tckn) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByTckn(tckn)
                .orElseThrow(() -> new UsernameNotFoundException("No customer with TCKN: " + tckn));

        return User.withUsername(tckn)
                .password(tckn)
                .roles(customer.getRole().name())
                .build();
    }
}
