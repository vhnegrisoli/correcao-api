package br.com.valorescorrecoesapi.modules.correcoes.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static br.com.valorescorrecoesapi.config.Constantes.DUAS_CASAS_DECIMAIS;

public class NumeroUtil {

    public static BigDecimal converterParaDuasCasasDecimais(Double valor) {
        return BigDecimal
            .valueOf(valor)
            .setScale(DUAS_CASAS_DECIMAIS, RoundingMode.HALF_UP);
    }
}
