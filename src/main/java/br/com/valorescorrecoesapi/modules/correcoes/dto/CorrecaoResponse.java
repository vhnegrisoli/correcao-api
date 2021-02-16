package br.com.valorescorrecoesapi.modules.correcoes.dto;

import br.com.valorescorrecoesapi.config.exception.Constantes;
import br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.valorescorrecoesapi.modules.correcoes.util.NumeroUtil.converterParaDuasCasasDecimais;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrecaoResponse {

    private Integer ano;
    @JsonFormat(pattern = Constantes.FORMATO_RETORNO_DATA)
    private LocalDate dataCorrecao;
    private ETipoCorrecao tipoCorrecao;
    private String tipoCorrecaoNome;
    private BigDecimal valorTotal;

    public static CorrecaoResponse gerar(Correcao correcao) {
        return CorrecaoResponse
            .builder()
            .ano(correcao.getAno())
            .dataCorrecao(correcao.getDataCorrecao())
            .tipoCorrecao(correcao.getTipoCorrecao())
            .tipoCorrecaoNome(correcao.getTipoCorrecao().getNomeTipoCorrecao())
            .valorTotal(converterParaDuasCasasDecimais(
                correcao.getTotalCorrigido() * correcao.getTipoCorrecao().getValorCorrecao().doubleValue()
                )
            )
            .build();
    }
}