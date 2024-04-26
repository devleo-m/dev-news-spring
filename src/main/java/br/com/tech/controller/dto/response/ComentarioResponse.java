package br.com.tech.controller.dto.response;

import java.time.LocalDateTime;

public record ComentarioResponse(
        Long id,
        String conteudo,
        LocalDateTime dataComentario,
        String usuario,
        String artigo
){}
