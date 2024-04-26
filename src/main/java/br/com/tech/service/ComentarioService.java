package br.com.tech.service;

import br.com.tech.controller.dto.request.ComentarioRequest;
import br.com.tech.controller.dto.response.ComentarioResponse;

import java.util.List;

public interface ComentarioService {
    ComentarioResponse criarComentario(ComentarioRequest request, String token);
    ComentarioResponse obterComentarioPorId(Long id);
    ComentarioResponse atualizarComentario(Long id, ComentarioRequest request);
    List<ComentarioResponse> listarTodosComentarios();
    void deletarComentario(Long id);
}
