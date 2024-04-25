package br.com.tech.controller.dto.response;

public record ArtigoResponse(
        Long id,
        String titulo,
        String conteudo,
        String resumo,
        String escritor
){}