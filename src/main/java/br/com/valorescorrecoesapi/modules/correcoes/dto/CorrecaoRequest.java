package br.com.valorescorrecoesapi.modules.correcoes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static br.com.valorescorrecoesapi.config.Constantes.ZERO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrecaoRequest {

    private LocalDate dataCorrecao;
    private Integer qtdNormal = ZERO;
    private Integer qtdTerceiraCorrecao = ZERO;
    private Integer qtdAvaliacaoDesempenho = ZERO;
}
