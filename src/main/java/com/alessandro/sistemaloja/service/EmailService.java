package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendOrderConfirmationEmail(Pedido obj);
    void sendEmail(SimpleMailMessage msg);
    void sendNewPasswordEmail(Cliente cliente, String newPass);
}
