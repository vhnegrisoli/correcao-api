package br.com.valorescorrecoesapi.modules.correcoes.model;

import br.com.valorescorrecoesapi.modules.autenticacao.model.Usuario;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoRequest;
import br.com.valorescorrecoesapi.modules.correcoes.enums.EProcessoSeletivo;
import br.com.valorescorrecoesapi.modules.correcoes.enums.EStatusCorrecao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.com.valorescorrecoesapi.modules.correcoes.enums.EStatusCorrecao.ABERTA;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CORRECAO")
public class Correcao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "ANO", length = 4, nullable = false)
    private Integer ano;

    @Column(name = "MES", length = 2, nullable = false)
    private Integer mes;

    @Column(name = "DIA", length = 2, nullable = false)
    private Integer dia;

    @Column(name = "DATA_CORRECAO", nullable = false)
    private LocalDate dataCorrecao;

    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "QTD_NORMAL", nullable = false)
    private Integer qtdNormal;

    @Column(name = "QTD_TERCEIRA_CORRECAO", nullable = false)
    private Integer qtdTerceiraCorrecao;

    @Column(name = "QTD_AVALIACAO_DESEMPENHO", nullable = false)
    private Integer qtdAvaliacaoDesempenho;

    @ManyToOne
    @JoinColumn(name = "FK_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatusCorrecao status;

    @Column(name = "PROCESSO_SELETIVO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EProcessoSeletivo processoSeletivo;

    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
        status = ABERTA;
    }

    public static Correcao gerarCorrecao(CorrecaoRequest request, Usuario usuario) {
        return Correcao
            .builder()
            .ano(request.getDataCorrecao().getYear())
            .mes(request.getDataCorrecao().getMonthValue())
            .dia(request.getDataCorrecao().getDayOfMonth())
            .dataCorrecao(request.getDataCorrecao())
            .qtdNormal(request.getQtdNormal())
            .qtdTerceiraCorrecao(request.getQtdTerceiraCorrecao())
            .qtdAvaliacaoDesempenho(request.getQtdAvaliacaoDesempenho())
            .usuario(usuario)
            .build();
    }
}
