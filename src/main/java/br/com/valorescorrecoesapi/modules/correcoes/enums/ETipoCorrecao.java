package br.com.valorescorrecoesapi.modules.correcoes.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum  ETipoCorrecao {

    NORMAL(BigDecimal.valueOf(3), "Correção Normal"),
    TERCEIRA_CORRECAO(BigDecimal.valueOf(3.3), "Terceira Correção"),
    AVALIACAO_DESEMPENHO(BigDecimal.valueOf(1.5), "Avaliação de Desempenho");

    private final BigDecimal valorCorrecao;
    private final String nomeTipoCorrecao;
}
