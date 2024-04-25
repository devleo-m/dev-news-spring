package br.com.tech.service.impl;

import br.com.tech.controller.dto.request.ArtigoRequest;
import br.com.tech.controller.dto.response.ArtigoResponse;
import br.com.tech.entity.ArtigoEntity;
import br.com.tech.entity.UsuarioEntity;
import br.com.tech.repository.ArtigoRepository;
import br.com.tech.repository.UsuarioRepository;
import br.com.tech.security.TokenService;
import br.com.tech.service.ArtigoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtigoServiceImpl implements ArtigoService {

    private final ArtigoRepository artigoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    @Override
    public ArtigoResponse criarArtigo(ArtigoRequest request, String token) {
        String email = tokenService.validateToken(token);

        UsuarioEntity escritor = usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Escritor nao encontrado"));

        if (!escritor.getId_papel().getNome().equals("ESCRITOR")){
            throw new RuntimeException("ESCRITOR nao encontrado");
        }

        ArtigoEntity artigoEntity = new ArtigoEntity();
        artigoEntity.setTitulo(request.titulo());
        artigoEntity.setConteudo(request.conteudo());
        artigoEntity.setResumo(request.resumo());
        artigoEntity.setId_usuario(escritor);

        ArtigoEntity artigo = artigoRepository.save(artigoEntity);

        return new ArtigoResponse(
                artigo.getId(),
                artigo.getTitulo(),
                artigo.getConteudo(),
                artigo.getResumo(),
                artigo.getId_usuario().getNome()
        );
    }

    @Override
    public ArtigoResponse obterArtigoPorId(Long id) {
        Optional<ArtigoEntity> artigoOptional = artigoRepository.findById(id);
        ArtigoEntity nota = artigoOptional.orElseThrow(()->
                new IllegalArgumentException("Nota não encontrada com o ID: " + id));
        return new ArtigoResponse(
                nota.getId(),
                nota.getTitulo(),
                nota.getConteudo(),
                nota.getResumo(),
                nota.getId_usuario().getNome()
        );
    }

    @Override
    public ArtigoResponse atualizarArtigo(Long id, ArtigoRequest request) {
        log.info("Atualizando Artigo pelo ID: {}", id);
        verificarExistenciaArtigo(id);

        ArtigoEntity artigoEntity = artigoRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("Turma não encontrado pelo ID: {}", id);
                    return new RuntimeException("Turma não encontrado com o ID: " + id);
                });

        artigoEntity.setTitulo(request.titulo());
        artigoEntity.setConteudo(request.conteudo());
        artigoEntity.setResumo(request.resumo());

        ArtigoEntity artigo = artigoRepository.save(artigoEntity);
        return new ArtigoResponse(
                artigo.getId(),
                artigo.getTitulo(),
                artigo.getConteudo(),
                artigo.getResumo(),
                artigo.getId_usuario().getNome()
        );
    }

    @Override
    public List<ArtigoResponse> listarTodosArtigos() {
        List<ArtigoEntity> artigos = artigoRepository.findAll();
        return artigos.stream()
                .map(artigo -> new ArtigoResponse(
                        artigo.getId(),
                        artigo.getTitulo(),
                        artigo.getConteudo(),
                        artigo.getResumo(),
                        artigo.getId_usuario().getNome()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deletarArtigo(Long id) {
        log.info("Deletando Turma com o ID: {}", id);
        verificarExistenciaArtigo(id);
        artigoRepository.deleteById(id);
    }

    private void verificarExistenciaArtigo(Long id) {
        if (!artigoRepository.existsById(id)) {
            log.warn("Artigo não encontrada com o ID: {}", id);
            throw new RuntimeException("Artigo não encontrada com o ID: " + id);
        } else {
            log.info("Artigo encontrada com o ID: {}", id);
        }
    }
}