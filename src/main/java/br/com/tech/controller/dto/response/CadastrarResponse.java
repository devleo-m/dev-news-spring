package br.com.tech.controller.dto.response;

import java.util.UUID;

public record CadastrarResponse(
        UUID id,
        String nome,
        String papel,
        String token
){}