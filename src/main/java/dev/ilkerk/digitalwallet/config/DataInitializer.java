package dev.ilkerk.digitalwallet.config;

import dev.ilkerk.digitalwallet.adapter.persistence.CustomerRepository;
import dev.ilkerk.digitalwallet.domain.entity.Customer;
import dev.ilkerk.digitalwallet.domain.entity.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadTestCustomer(CustomerRepository customerRepository) {
        return args -> {
            String tckn = "12345678901";

            // Prevent duplicate inserts
            if (customerRepository.findByTckn(tckn).isEmpty()) {
                Customer testCustomer = Customer.builder()
                        .name("Test")
                        .surname("Customer")
                        .tckn(tckn)
                        .role(Role.CUSTOMER)
                        .build();

                customerRepository.save(testCustomer);
                System.out.println("✔ Test customer created: " + tckn);
            }
        };
    }
}
