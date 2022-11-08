package com.alessandro.sistemaloja.config;

import com.alessandro.sistemaloja.service.DBService;
import com.alessandro.sistemaloja.service.EmailService;
import com.alessandro.sistemaloja.service.MockEmailService;
import com.alessandro.sistemaloja.service.SmtpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;

@Configuration
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDataBase() throws ParseException {
        dbService.instantiateTestDataBase();
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
        //return new SmtpEmailService();
    }
}
