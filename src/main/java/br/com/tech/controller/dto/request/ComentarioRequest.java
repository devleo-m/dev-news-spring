package br.com.tech.controller.dto.request;

public record ComentarioRequest(
        String conteudo,
        Long idArtigo
){}
