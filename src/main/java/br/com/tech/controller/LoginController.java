package br.com.tech.controller;

import br.com.tech.controller.dto.request.LoginRequest;
import br.com.tech.controller.dto.response.LoginResponse;
import br.com.tech.entity.UsuarioEntity;
import br.com.tech.repository.UsuarioRepository;
import br.com.tech.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping()
    public ResponseEntity login(@RequestBody LoginRequest body) {
        // Busca o usuário no banco de dados pelo login fornecido no corpo da requisição
        UsuarioEntity usuario = usuarioRepository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se a senha fornecida no corpo da requisição corresponde à senha armazenada para o usuário
        // Aceita tanto senhas criptografadas quanto não criptografadas
        boolean senhaValida = passwordEncoder.matches(body.senha(), usuario.getSenha()) || body.senha().equals(usuario.getSenha());

        if (senhaValida) {
            String token = tokenService.generateToken(usuario); // Gera um token de autenticação para o usuário
            return ResponseEntity.ok(
                    new LoginResponse(
                            usuario.getNome(),
                            usuario.getEmail(),
                            usuario.getId_papel().getNome(),
                            token
                    ));
        }
        // Retorna uma resposta de erro se a senha fornecida não corresponder à senha armazenada
        return ResponseEntity.badRequest().build();
    }
}