package br.com.tech.service.impl;

import br.com.tech.controller.dto.request.ComentarioRequest;
import br.com.tech.controller.dto.response.ComentarioResponse;
import br.com.tech.entity.ArtigoEntity;
import br.com.tech.entity.ComentarioEntity;
import br.com.tech.entity.UsuarioEntity;
import br.com.tech.repository.ArtigoRepository;
import br.com.tech.repository.ComentarioRepository;
import br.com.tech.repository.UsuarioRepository;
import br.com.tech.security.TokenService;
import br.com.tech.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final ArtigoRepository artigoRepository;

    @Override
    public ComentarioResponse criarComentario(ComentarioRequest request, String token) {
        String email = tokenService.validateToken(token);

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado"));

        ArtigoEntity artigo = artigoRepository.findById(request.idArtigo())
                .orElseThrow(()-> new RuntimeException("Artigo não encontrado"));

        ComentarioEntity comentarioEntity = new ComentarioEntity();
        comentarioEntity.setConteudo(request.conteudo());
        comentarioEntity.setId_usuario(usuario);
        comentarioEntity.setId_artigo(artigo);

        ComentarioEntity comentario = comentarioRepository.save(comentarioEntity);

        return new ComentarioResponse(
                comentario.getId(),
                comentario.getConteudo(),
                comentario.getDataCadastro(),
                comentario.getId_usuario().getNome(),
                comentario.getId_artigo().getTitulo()
                );
    }

    @Override
    public ComentarioResponse obterComentarioPorId(Long id) {
        Optional<ComentarioEntity> comentarioOpcional = comentarioRepository.findById(id);
        ComentarioEntity comentario = comentarioOpcional.orElseThrow(()->
                new IllegalArgumentException("Comentrio não encontrada com o ID: " + id));
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getConteudo(),
                comentario.getDataCadastro(),
                comentario.getId_usuario().getNome(),
                (comentario.getId_artigo() != null) ? comentario.getId_artigo().getTitulo() : null
        );
    }

    @Override
    public ComentarioResponse atualizarComentario(Long id, ComentarioRequest request) {
        log.info("Atualizando Comentario pelo ID: {}", id);
        verificarExistenciaComentario(id);

        ComentarioEntity comentarioEntity = comentarioRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("Comentario não encontrado pelo ID: {}", id);
                    return new RuntimeException("Comentario não encontrado com o ID: " + id);
                });

        comentarioEntity.setConteudo(request.conteudo());

        ComentarioEntity comentario = comentarioRepository.save(comentarioEntity);
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getConteudo(),
                comentario.getDataCadastro(),
                comentario.getId_usuario().getNome(),
                (comentario.getId_artigo() != null) ? comentario.getId_artigo().getTitulo() : null
        );
    }

    @Override
    public List<ComentarioResponse> listarTodosComentarios() {
        List<ComentarioEntity> comentarios = comentarioRepository.findAll();
        return comentarios.stream()
                .map(comentario -> new ComentarioResponse(
                        comentario.getId(),
                        comentario.getConteudo(),
                        comentario.getDataCadastro(),
                        comentario.getId_usuario().getNome(),
                        (comentario.getId_artigo() != null) ? comentario.getId_artigo().getTitulo() : null
                ))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deletarComentario(Long id) {
        log.info("Deletando comentario com o ID: {}", id);
        verificarExistenciaComentario(id);
        comentarioRepository.deleteById(id);
    }

    private void verificarExistenciaComentario(Long id) {
        if (!comentarioRepository.existsById(id)) {
            log.warn("Comentario não encontrada com o ID: {}", id);
            throw new RuntimeException("Comentario não encontrada com o ID: " + id);
        } else {
            log.info("Comentario encontrada com o ID: {}", id);
        }
    }
}
