package br.com.tech.controller.dto.request;

public record ArtigoRequest(
        String titulo,
        String conteudo,
        String resumo
){}