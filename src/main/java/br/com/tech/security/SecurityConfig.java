package br.com.tech.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando a cadeia de filtros de segurança");
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cadastrar").permitAll()

                        .requestMatchers(HttpMethod.GET, "/artigos").permitAll() // Todos podem ler os artigos
                        .requestMatchers(HttpMethod.POST, "/artigos").hasAnyRole("ADMIN", "ESCRITOR")
                        .requestMatchers(HttpMethod.PUT, "/artigos").hasAnyRole("ADMIN", "ESCRITOR")

                        .requestMatchers(HttpMethod.GET, "/comentarios").hasAnyRole("ADMIN", "ESCRITOR", "CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/comentarios").hasAnyRole("ADMIN", "ESCRITOR", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/comentarios").hasAnyRole("ADMIN", "ESCRITOR", "CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/comentarios").hasAnyRole("ADMIN", "ESCRITOR", "CLIENTE")

                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("Cadeia de filtros de segurança configurada");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Configurando a cadeia de password encoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        log.info("Configurando a cadeia de authentication manager");
        return authenticationConfiguration.getAuthenticationManager();
    }
}
