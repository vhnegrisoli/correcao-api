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
public class CorrecaoDetalheResponse {

    private Integer id;
    @JsonFormat(pattern = Constantes.FORMATO_RETORNO_DATA)
    private LocalDate dataCorrecao;
    private Integer totalCorrigido;
    private BigDecimal valorTotal;
    private Integer qtdNormal;
    private BigDecimal valorRecebidoNormal;
    private Integer qtdTerceiraCorrecao;
    private BigDecimal valorRecebidoTerceiraCorrecao;
    private Integer qtdAvaliacaoDesempenho;
    private BigDecimal valorRecebidoAvaliacaoDesempenho;

    public static CorrecaoDetalheResponse gerar(Correcao correcao) {
        var total = correcao.getQtdNormal() + correcao.getQtdTerceiraCorrecao() + correcao.getQtdAvaliacaoDesempenho();
        return CorrecaoDetalheResponse
            .builder()
            .id(correcao.getId())
            .dataCorrecao(correcao.getDataCorrecao())
            .totalCorrigido(total)
            .valorTotal(converterParaDuasCasasDecimais(
                (correcao.getQtdNormal() * NORMAL.getValorCorrecao().doubleValue())
                    + (correcao.getQtdTerceiraCorrecao() * TERCEIRA_CORRECAO.getValorCorrecao().doubleValue())
                    + (correcao.getQtdAvaliacaoDesempenho() * AVALIACAO_DESEMPENHO.getValorCorrecao().doubleValue())))
            .qtdNormal(correcao.getQtdNormal())
            .valorRecebidoNormal(converterParaDuasCasasDecimais(
                correcao.getQtdNormal() * NORMAL.getValorCorrecao().doubleValue()))
            .qtdTerceiraCorrecao(correcao.getQtdTerceiraCorrecao())
            .valorRecebidoTerceiraCorrecao(converterParaDuasCasasDecimais(
                correcao.getQtdTerceiraCorrecao() * TERCEIRA_CORRECAO.getValorCorrecao().doubleValue()))
            .qtdAvaliacaoDesempenho(correcao.getQtdAvaliacaoDesempenho())
            .valorRecebidoAvaliacaoDesempenho(converterParaDuasCasasDecimais(
                correcao.getQtdAvaliacaoDesempenho() * AVALIACAO_DESEMPENHO.getValorCorrecao().doubleValue()))
            .build();
    }
}