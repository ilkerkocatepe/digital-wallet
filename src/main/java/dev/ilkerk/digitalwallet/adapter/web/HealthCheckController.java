package dev.ilkerk.digitalwallet.adapter.web;

import dev.ilkerk.digitalwallet.config.HealthCheckConfiguration;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("healthcheck")
@RequiredArgsConstructor
@Tag(name = "Health Check", description = "Health Check API")
public class HealthCheckController {
    private final HealthCheckConfiguration healthCheckConfiguration;

    @GetMapping
    public String healthCheck() {
        return "OK!";
    }

    @GetMapping("essentials")
    public String healthCheckEssentials() {
        return healthCheckConfiguration.healthCheck();
    }

}
