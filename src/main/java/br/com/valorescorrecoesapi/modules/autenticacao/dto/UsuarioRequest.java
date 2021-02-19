package br.com.valorescorrecoesapi.modules.autenticacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioRequest {

    private String usuario;
    private String senha;
}
