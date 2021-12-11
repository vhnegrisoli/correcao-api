package br.com.valorescorrecoesapi.modules.correcoes.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EProcessoSeletivo {

    ENEM_2020("Processo Enem 2020"),
    ENEM_2021("Processo Enem 2021");

    private final String processo;
}
