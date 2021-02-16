package br.com.valorescorrecoesapi.modules.correcoes.dto;

import br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrecaoRequest {

    private LocalDate dataCorrecao;
    private ETipoCorrecao tipoCorrecao;
    private Integer totalCorrigido;
}
