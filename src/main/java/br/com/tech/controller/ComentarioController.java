package br.com.tech.controller;

import br.com.tech.controller.dto.request.ComentarioRequest;
import br.com.tech.controller.dto.response.ComentarioResponse;
import br.com.tech.entity.ComentarioEntity;
import br.com.tech.service.ComentarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comentarios")
public class ComentarioController {
    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<ComentarioResponse> criarArtigo(@RequestBody ComentarioRequest request,
                                                          HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization"); // Recupere o token do cabeçalho de autorização

        if (token != null && token.startsWith("Bearer ")) { // Remova o prefixo "Bearer " do token
            token = token.substring(7);
        }
        log.info("Criando comentario: {}", request);
        ComentarioResponse comentario = comentarioService.criarComentario(request, token);
        log.debug("Comentario criado: {}", comentario);
        return ResponseEntity.status(HttpStatus.CREATED).body(comentario);
    }


    @GetMapping
    public ResponseEntity<List<ComentarioResponse>> listarTodosArtigos() {
        log.info("Listando todos os comentarios");
        List<ComentarioResponse> comentarios = comentarioService.listarTodosComentarios();
        log.debug("Quantidade de comentarios encontrados: {}", comentarios.size());
        return ResponseEntity.status(HttpStatus.OK).body(comentarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponse> obterArtigoPorId(@PathVariable Long id) {
        log.info("Buscando comentario por ID: {}", id);
        ComentarioResponse comentario = comentarioService.obterComentarioPorId(id);
        log.debug("comentario: {}", comentario);
        return ResponseEntity.status(HttpStatus.OK).body(comentario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComentarioResponse> atualizarArtigo(@PathVariable Long id,
                                                          @RequestBody ComentarioRequest request) {
        log.info("Atualizando comentario: {}", request);
        ComentarioResponse novoComentario = comentarioService.atualizarComentario(id, request);
        log.debug("Comentario atualizado: {}", novoComentario);
        return ResponseEntity.status(HttpStatus.OK).body(novoComentario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ComentarioEntity> deletarArtigo(@PathVariable Long id) {
        log.info("Deletando comentario: {}", id);
        comentarioService.deletarComentario(id);
        log.debug("Comentario {} deletado com sucesso!", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
