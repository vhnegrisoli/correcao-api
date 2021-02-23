package br.com.valorescorrecoesapi.modules.correcoes.dto;

import br.com.valorescorrecoesapi.config.Constantes;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import static br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao.*;
import static br.com.valorescorrecoesapi.modules.correcoes.util.NumeroUtil.converterParaDuasCasasDecimais;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrecoesDiarias {

    private String dia;

    private BigDecimal valor;

    public static CorrecoesDiarias converterDe(Correcao correcao) {
        return CorrecoesDiarias
            .builder()
            .dia(correcao.getDataCorrecao().format(DateTimeFormatter.ofPattern(Constantes.FORMATO_RETORNO_DATA)))
            .valor(converterParaDuasCasasDecimais(
                (correcao.getQtdNormal() * NORMAL.getValorCorrecao().doubleValue())
                    + (correcao.getQtdTerceiraCorrecao() * TERCEIRA_CORRECAO.getValorCorrecao().doubleValue())
                    + (correcao.getQtdAvaliacaoDesempenho() * AVALIACAO_DESEMPENHO.getValorCorrecao().doubleValue()))
            )
            .build();
    }
}
