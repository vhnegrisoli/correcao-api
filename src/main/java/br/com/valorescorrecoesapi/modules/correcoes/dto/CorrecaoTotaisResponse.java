package br.com.valorescorrecoesapi.modules.correcoes.dto;

import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import static br.com.valorescorrecoesapi.config.Constantes.ZERO;
import static br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao.*;
import static br.com.valorescorrecoesapi.modules.correcoes.util.NumeroUtil.converterParaDuasCasasDecimais;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrecaoTotaisResponse {

    private Integer totalCorrecoes = ZERO;
    private BigDecimal valorTotalRecebido = BigDecimal.ZERO;
    private Long totalDiasCorridos = ZERO.longValue();

    public static CorrecaoTotaisResponse gerar(List<Correcao> correcoes) {
        var totais = new CorrecaoTotaisResponse();
        totais.calcularValorTotal(correcoes);
        totais.setTotalDiasCorridos(correcoes
            .stream()
            .map(Correcao::getDataCorrecao)
            .distinct()
            .count()
        );
        return totais;
    }

    private void calcularValorTotal(List<Correcao> correcoes) {
        var totalNormais = buscarTotaisNormais(correcoes);
        var totalTerceirasCorrecoes = buscarTotaisTerceirasCorrecoes(correcoes);
        var totalAvaliacoesDesempenho = buscarTotaisAvaliacoesDesempenho(correcoes);
        totalCorrecoes = totalNormais + totalTerceirasCorrecoes + totalAvaliacoesDesempenho;
        valorTotalRecebido = converterParaDuasCasasDecimais(
            (totalNormais * NORMAL.getValorCorrecao().doubleValue())
                + (totalTerceirasCorrecoes * TERCEIRA_CORRECAO.getValorCorrecao().doubleValue())
                + (totalAvaliacoesDesempenho * AVALIACAO_DESEMPENHO.getValorCorrecao().doubleValue())
        );
    }

    private Integer buscarTotaisNormais(List<Correcao> correcoes) {
        return correcoes
            .stream()
            .map(Correcao::getQtdNormal)
            .reduce(ZERO, Integer::sum);
    }

    private Integer buscarTotaisTerceirasCorrecoes(List<Correcao> correcoes) {
        return correcoes
            .stream()
            .map(Correcao::getQtdTerceiraCorrecao)
            .reduce(ZERO, Integer::sum);
    }

    private Integer buscarTotaisAvaliacoesDesempenho(List<Correcao> correcoes) {
        return correcoes
            .stream()
            .map(Correcao::getQtdAvaliacaoDesempenho)
            .reduce(ZERO, Integer::sum);
    }
}
