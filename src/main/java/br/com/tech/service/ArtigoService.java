package br.com.tech.service;

import br.com.tech.controller.dto.request.ArtigoRequest;
import br.com.tech.controller.dto.response.ArtigoResponse;

import java.util.List;

public interface ArtigoService {
    ArtigoResponse criarArtigo(ArtigoRequest request, String token);
    ArtigoResponse obterArtigoPorId(Long id);
    ArtigoResponse atualizarArtigo(Long id, ArtigoRequest request);
    List<ArtigoResponse> listarTodosArtigos();
    void deletarArtigo(Long id);
}