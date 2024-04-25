package br.com.tech.controller.dto.request;

public record LoginRequest(
        String email,
        String senha
){}