package br.com.valorescorrecoesapi.modules.correcoes.dto;

import br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static br.com.valorescorrecoesapi.config.exception.Constantes.ZERO;
import static br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao.*;
import static br.com.valorescorrecoesapi.modules.correcoes.util.NumeroUtil.converterParaDuasCasasDecimais;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrecaoTotaisResponse {

    private Integer anoAtual = LocalDate.now().getYear();
    private Integer totalCorrecoesCorrigidas = ZERO;
    private Integer totalNormais = ZERO;
    private Integer totalTerceirasCorrecoes = ZERO;
    private Integer totalAvaliacoesDesempenho = ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public static CorrecaoTotaisResponse gerar(List<Correcao> correcoes) {
        var totais = new CorrecaoTotaisResponse();
        totais.setTotalNormais(buscarTotaisPorTipo(correcoes, NORMAL));
        totais.setTotalTerceirasCorrecoes(buscarTotaisPorTipo(correcoes, TERCEIRA_CORRECAO));
        totais.setTotalAvaliacoesDesempenho(buscarTotaisPorTipo(correcoes, AVALIACAO_DESEMPENHO));
        totais.calcularValorTotal();
        return totais;
    }

    private static Integer buscarTotaisPorTipo(List<Correcao> correcoes, ETipoCorrecao tipoCorrecao) {
        return correcoes
            .stream()
            .filter(correcao -> tipoCorrecao.equals(correcao.getTipoCorrecao()))
            .map(Correcao::getTotalCorrigido)
            .reduce(ZERO, Integer::sum);
    }

    private void calcularValorTotal() {
        totalCorrecoesCorrigidas = totalNormais + totalTerceirasCorrecoes + totalAvaliacoesDesempenho;
        valorTotal = converterParaDuasCasasDecimais(
            (totalNormais * NORMAL.getValorCorrecao().doubleValue())
                + (totalTerceirasCorrecoes * TERCEIRA_CORRECAO.getValorCorrecao().doubleValue())
                + (totalAvaliacoesDesempenho * AVALIACAO_DESEMPENHO.getValorCorrecao().doubleValue())
        );
    }
}
