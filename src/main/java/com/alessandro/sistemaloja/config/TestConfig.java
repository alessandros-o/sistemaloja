package com.alessandro.sistemaloja.config;

import com.alessandro.sistemaloja.service.EmailService;
import com.alessandro.sistemaloja.service.MockEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }
}
