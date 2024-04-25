package br.com.tech.controller;

import br.com.tech.controller.dto.request.ArtigoRequest;
import br.com.tech.controller.dto.response.ArtigoResponse;
import br.com.tech.entity.ArtigoEntity;
import br.com.tech.service.ArtigoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/artigos")
public class ArtigoController {
    private static final Logger log = LoggerFactory.getLogger(ArtigoController.class);
    private final ArtigoService artigoService;

    @PostMapping
    public ResponseEntity<ArtigoResponse> criarArtigo(@RequestBody ArtigoRequest request,
                                                      HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization"); // Recupere o token do cabeçalho de autorização

        if (token != null && token.startsWith("Bearer ")) { // Remova o prefixo "Bearer " do token
            token = token.substring(7);
        }

        log.info("Criando artigo: {}", request);
        ArtigoResponse artigo = artigoService.criarArtigo(request, token);
        log.debug("Artigo criado: {}", artigo);
        return ResponseEntity.status(HttpStatus.CREATED).body(artigo);
    }


    @GetMapping
    public ResponseEntity<List<ArtigoResponse>> listarTodosArtigos() {
        log.info("Listando todos os artigos");
        List<ArtigoResponse> artigos = artigoService.listarTodosArtigos();
        log.debug("Quantidade de artigos encontrados: {}", artigos.size());
        return ResponseEntity.status(HttpStatus.OK).body(artigos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtigoResponse> obterArtigoPorId(@PathVariable Long id) {
        log.info("Buscando artigo por ID: {}", id);
        ArtigoResponse artigo = artigoService.obterArtigoPorId(id);
        log.debug("Artigo: {}", artigo);
        return ResponseEntity.status(HttpStatus.OK).body(artigo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtigoResponse> atualizarArtigo(@PathVariable Long id,
                                                        @RequestBody ArtigoRequest request) {
        log.info("Atualizando artigo: {}", request);
        ArtigoResponse novoArtigo = artigoService.atualizarArtigo(id, request);
        log.debug("Artigo atualizado: {}", novoArtigo);
        return ResponseEntity.status(HttpStatus.OK).body(novoArtigo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArtigoEntity> deletarArtigo(@PathVariable Long id) {
        log.info("Deletando artigo: {}", id);
        artigoService.deletarArtigo(id);
        log.debug("Artigo {} deletado com sucesso!", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
