package br.com.tech.controller;

import br.com.tech.controller.dto.request.CadastrarRequest;
import br.com.tech.controller.dto.response.CadastrarResponse;
import br.com.tech.entity.PapelEntity;
import br.com.tech.entity.UsuarioEntity;
import br.com.tech.repository.PapelRepository;
import br.com.tech.repository.UsuarioRepository;
import br.com.tech.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/cadastrar")
@RequiredArgsConstructor
public class CadastrarController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final PapelRepository papelRepository;

    /**
     * Endpoint para registrar um novo usuário.
     *
     * @param request O corpo da requisição contendo as informações do novo usuário a ser registrado.
     * @return ResponseEntity contendo os detalhes do novo usuário e um token de autenticação, se o registro for bem-sucedido,
     *         ou uma resposta de erro se o usuário já existir no banco de dados.
     */
    @PostMapping()
    public ResponseEntity registrar(@RequestBody CadastrarRequest request) {

        Optional<UsuarioEntity> usuarioOptional = usuarioRepository.findByEmail(request.email()); // Verifica se já existe um usuário com o mesmo login no banco de dados
        if(usuarioOptional.isEmpty()) { // Se não houver um usuário com o mesmo login, registra o novo usuário

            if (request.senha() == null || request.senha().isEmpty()) { // Verifica se a senha fornecida não é nula ou vazia
                return ResponseEntity.badRequest().body("Senha não pode ser nula ou vazia");
            }

            UsuarioEntity novoUsuario = new UsuarioEntity(); // Cria uma nova entidade de usuário
            novoUsuario.setNome(request.nome());
            novoUsuario.setSenha(passwordEncoder.encode(request.senha()));
            novoUsuario.setEmail(request.email());

            PapelEntity papel = determinarPapel(request.papel());
            novoUsuario.setId_papel(papel);

            novoUsuario = usuarioRepository.save(novoUsuario); // Salva o novo usuário no banco de dados
            UUID userId = novoUsuario.getId(); // Obter o ID gerado

            String token = tokenService.generateToken(novoUsuario); // Gera um token de autenticação para o novo usuário

            return ResponseEntity.ok(new CadastrarResponse(
                    userId,
                    novoUsuario.getEmail(),
                    papel.getNome(),
                    token
            ));
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Determina o papel do usuário com base no tipo de usuário fornecido.
     *
     * @param papel O tipo de usuário fornecido.
     * @return O papel correspondente ao tipo de usuário fornecido.
     * @throws RuntimeException Se o tipo de usuário não for especificado, não corresponder a nenhum papel existente
     *                          ou se ocorrer algum erro durante a busca.
     */
    private PapelEntity determinarPapel(String papel) {
        if (papel == null || papel.isEmpty()) {
            return papelRepository.findByNome("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Papel 'CLIENTE' não encontrado"));
        }
        Optional<PapelEntity> papelOptional = papelRepository.findByNome(papel);
        if (papelOptional.isPresent()) {
            return papelOptional.get();
        } else {
            throw new RuntimeException("papel '" + papel + "' não encontrado");
        }
    }
}