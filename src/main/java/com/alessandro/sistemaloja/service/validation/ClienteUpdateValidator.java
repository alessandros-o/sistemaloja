package com.alessandro.sistemaloja.service.validation;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.domain.enums.TipoCliente;
import com.alessandro.sistemaloja.dto.ClienteCreateRequest;
import com.alessandro.sistemaloja.dto.ClienteResponse;
import com.alessandro.sistemaloja.repository.ClienteRepository;
import com.alessandro.sistemaloja.resources.exception.FieldMessage;
import com.alessandro.sistemaloja.service.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteResponse> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ClienteRepository repository;

    @Override
    public void initialize(ClienteUpdate constraintAnnotation) {}

    @Override
    public boolean isValid(ClienteResponse clienteResponse, ConstraintValidatorContext context) {

        // Para pegar o id através da URI
        //(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) pega o map de variáveis de URI que estão na requisição
        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        Cliente aux = repository.findByEmail(clienteResponse.getEmail());
        // verifica se o email passado pertence ao id do cliente a ser atualizado ou a outro cliente já existente no BD
        // se pertencer a outro cliente, ou seja, se o id for diferente, lança exceção
        if (aux != null && !aux.getId().equals(uriId)) {
            list.add(new FieldMessage("email", "Email já existente"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
