package br.com.tech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artigo")
public class ArtigoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false)
    private String conteudo;

    @Column(nullable = true, length = 255)
    private String resumo;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(nullable = true, length = 255)
    private String tags;

    @Column(nullable = true, length = 255)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private UsuarioEntity id_usuario;
}
