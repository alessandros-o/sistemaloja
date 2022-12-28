package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Cidade;
import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.domain.Endereco;
import com.alessandro.sistemaloja.domain.enums.Perfil;
import com.alessandro.sistemaloja.domain.enums.TipoCliente;
import com.alessandro.sistemaloja.dto.AuthenticatedUserDetails;
import com.alessandro.sistemaloja.dto.ClienteCreateRequest;
import com.alessandro.sistemaloja.dto.ClienteResponse;
import com.alessandro.sistemaloja.repository.ClienteRepository;
import com.alessandro.sistemaloja.repository.EnderecoRepository;
import com.alessandro.sistemaloja.service.exception.AuthorizationException;
import com.alessandro.sistemaloja.service.exception.DataIntegrityException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Value("${img.prefix.client.profile}")
    private String prefix;


    @Value("${img.profile.size}")
    private Integer size;

    private final PasswordEncoder pe;
    private final ClienteRepository repo;
    private final EnderecoRepository enderecoRepository;

    private S3Service s3Service;

    private ImageService imageService;

    public ClienteService(ClienteRepository repo, EnderecoRepository enderecoRepository, PasswordEncoder pe, S3Service s3Service, ImageService imageService) {
        this.repo = repo;
        this.enderecoRepository = enderecoRepository;
        this.pe = pe;
        this.s3Service = s3Service;
        this.imageService = imageService;
    }

    public Cliente buscarPorId(Integer id) {

//        AuthenticatedUserDetails user = UserDetailsService.authenticated();
//        if (user == null || !user.perfis().contains(Perfil.ADMIN.getDescricao()) && !Objects.equals(id, user.id())) {
//            throw  new AuthorizationException("Acesso negado");
//        }

        Optional<Cliente> clienteOptional = repo.findById(id);
        return clienteOptional.orElseThrow(() -> new ObjectNotFoundException(Cliente.class, "Cliente com id: " + id +  " não encontrado!"));
    }

    public Cliente buscarPorEmail(String email) {
        AuthenticatedUserDetails user = UserDetailsService.authenticated();
        if (user == null || !user.perfis().contains(Perfil.ADMIN.getDescricao()) && !Objects.equals(email, user.email())) {
            throw  new AuthorizationException("Acesso negado");
        }

        Cliente obj = repo.findByEmail(email);
        if (obj == null) {
            throw new ObjectNotFoundException(Cliente.class, "Objeto não encontrado");
        }
        return obj;
    }

    @Transactional
    public Cliente inserir(Cliente obj) {
        obj = repo.save(obj);
        enderecoRepository.saveAll(obj.getEnderecos());
        return obj;
    }

    public Cliente alterar(Cliente obj) {
        var newObj = buscarPorId(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos relacionados");
        }

    }

    public List<ClienteResponse> listarTodas() {
        var list = repo.findAll();
        List<ClienteResponse> responseList = list.stream().map(x -> new ClienteResponse(x)).collect(Collectors.toList());
        return  responseList;
    }

    public Page<ClienteResponse> buscarPaginado(Integer page, Integer qtdPorPagina, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, qtdPorPagina, Sort.Direction.valueOf(direction), orderBy);
        var objetoPaginado = repo.findAll(pageRequest);
        Page<ClienteResponse> responsePage = objetoPaginado.map(x -> new ClienteResponse(x));
        return responsePage;
    }

    public Cliente fromDTO(ClienteResponse response) {

        return new Cliente(response.getId(), response.getNome(), response.getEmail(), null, null, null);
    }

    public Cliente fromDTO(ClienteCreateRequest createRequest) {
        Cliente cliente = new Cliente(null, createRequest.getNome(), createRequest.getEmail(), createRequest.getCpfOuCnpj(), TipoCliente.toEnum(createRequest.getTipo()), pe.encode(createRequest.getSenha()));
        Cidade cidade = new Cidade(createRequest.getCidadeId(), null, null);
        Endereco endereco = new Endereco(null, createRequest.getLogradouro(), createRequest.getNumero(), createRequest.getComplemento(), createRequest.getBairro(), createRequest.getCep(), cliente, cidade);
        cliente.getEnderecos().add(endereco);
        cliente.getTelefones().add(createRequest.getTelefone1());
        if (createRequest.getTelefone2() != null) {
            cliente.getTelefones().add(createRequest.getTelefone2());
        }
        if (createRequest.getTelefone3() != null) {
            cliente.getTelefones().add(createRequest.getTelefone3());
        }
        return cliente;
    }

    private void updateData(Cliente newObj, Cliente obj) {
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }

    public URI uploadProfilePicture(MultipartFile multipartFile) {
        AuthenticatedUserDetails user = UserDetailsService.authenticated();
        if (user == null) {
            throw  new AuthorizationException("Acesso negado");
        }

        //extrair um JPG a partir da imagem que foi enviada na requisição(parâmetro) do método
        BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);

        //recortando imagem para ficar quadrada
        jpgImage = imageService.croparImagem(jpgImage);

        //redimensionando a imagem
        jpgImage = imageService.redimensionarImagem(jpgImage, size);

        //montando o nome do arquivo personalizado de acordo com o cliente pegando prefixo definido no application.properties
        String fileName = prefix + user.id() + ".jpg";

        return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
    }
}
