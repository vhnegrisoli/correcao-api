package br.com.valorescorrecoesapi.modules.correcoes.dto;

import br.com.valorescorrecoesapi.config.Constantes;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao.*;
import static br.com.valorescorrecoesapi.modules.correcoes.util.NumeroUtil.converterParaDuasCasasDecimais;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrecaoResponse {

    private Integer id;
    @JsonFormat(pattern = Constantes.FORMATO_RETORNO_DATA)
    private LocalDate dataCorrecao;
    private Integer totalCorrigido;
    private BigDecimal valorTotal;

    public static CorrecaoResponse gerar(Correcao correcao) {
        var total = correcao.getQtdNormal() + correcao.getQtdTerceiraCorrecao() + correcao.getQtdAvaliacaoDesempenho();
        return CorrecaoResponse
            .builder()
            .id(correcao.getId())
            .dataCorrecao(correcao.getDataCorrecao())
            .totalCorrigido(total)
            .valorTotal(converterParaDuasCasasDecimais(
                (correcao.getQtdNormal() * NORMAL.getValorCorrecao().doubleValue())
                    + (correcao.getQtdTerceiraCorrecao() * TERCEIRA_CORRECAO.getValorCorrecao().doubleValue())
                    + (correcao.getQtdAvaliacaoDesempenho() * AVALIACAO_DESEMPENHO.getValorCorrecao().doubleValue()))
            )
            .build();
    }
}