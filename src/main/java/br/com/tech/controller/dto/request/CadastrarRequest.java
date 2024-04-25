package br.com.tech.controller.dto.request;

public record CadastrarRequest(
        String nome,
        String email,
        String senha,
        String papel
){}