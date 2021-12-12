package br.com.valorescorrecoesapi.modules.correcoes.dto;

import br.com.valorescorrecoesapi.modules.correcoes.enums.EProcessoSeletivo;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrecaoHistoricoResponse {

    private EProcessoSeletivo processoSeletivo;
    private String processoSeletivoDetalhe;

    public static CorrecaoHistoricoResponse gerar(Correcao correcao) {
        return CorrecaoHistoricoResponse
            .builder()
            .processoSeletivo(correcao.getProcessoSeletivo())
            .processoSeletivoDetalhe(correcao.getProcessoSeletivo().getProcesso())
            .build();
    }
}