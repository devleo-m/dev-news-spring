package br.com.tech.security;

import br.com.tech.entity.UsuarioEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UsuarioEntity user) {
        log.info("Gerando token para o usuário: {}", user.getEmail());
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("login-auth-api")
                .withSubject(user.getEmail())
                .withExpiresAt(Date.from(this.generateExpirationDate()))
                .sign(algorithm);
    }

    public String validateToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token de validação é nulo");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build();
            return verifier.verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao verificar token", exception);
        }
    }

    private Instant generateExpirationDate(){
        log.info("Generando expiration date");
        return LocalDateTime.now().plusHours(90).toInstant(ZoneOffset.of("-03:00"));
    }
}