package com.alessandro.sistemaloja.resource;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.dto.ClienteCreateRequest;
import com.alessandro.sistemaloja.dto.ClienteResponse;
import com.alessandro.sistemaloja.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    private final ClienteService clienteService;

    public ClienteResource(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        Cliente obj = clienteService.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> buscarPorEmail(@PathVariable String email) {
        Cliente obj = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodas() {
        List<ClienteResponse> list = clienteService.listarTodas();
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/page")
    public ResponseEntity<Page<ClienteResponse>> listarTodasPaginado(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "qtdPorPagina", defaultValue = "24") Integer qtdPorPagina,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<ClienteResponse> list = clienteService.buscarPaginado(page, qtdPorPagina, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }


    @PostMapping
    public ResponseEntity<Void> inserir(@Valid @RequestBody ClienteCreateRequest createRequest) {
        Cliente obj = clienteService.fromDTO(createRequest);
        obj = clienteService.inserir(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterar(@PathVariable Integer id, @Valid @RequestBody ClienteResponse objResponse) {
        Cliente obj = clienteService.fromDTO(objResponse);
        obj.setId(id);
        obj = clienteService.alterar(obj);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/picture")
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file")MultipartFile file) {
        URI uri = clienteService.uploadProfilePicture(file);
        return ResponseEntity.created(uri).build();
    }
}
