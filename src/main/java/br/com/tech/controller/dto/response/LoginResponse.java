package br.com.tech.controller.dto.response;

public record LoginResponse(
        String nome,
        String email,
        String papel,
        String token
){}