package com.alessandro.sistemaloja.service.validation;

import com.alessandro.sistemaloja.domain.enums.TipoCliente;
import com.alessandro.sistemaloja.dto.ClienteCreateRequest;
import com.alessandro.sistemaloja.resources.exception.FieldMessage;
import com.alessandro.sistemaloja.service.validation.utils.BR;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteCreateRequest> {

    @Override
    public void initialize(ClienteInsert constraintAnnotation) {}

    @Override
    public boolean isValid(ClienteCreateRequest createRequest, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if (createRequest.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(createRequest.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }

        if (createRequest.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(createRequest.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
