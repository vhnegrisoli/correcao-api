package br.com.valorescorrecoesapi.modules.autenticacao.controller;

import br.com.valorescorrecoesapi.modules.autenticacao.dto.JwtResponse;
import br.com.valorescorrecoesapi.modules.autenticacao.dto.UsuarioRequest;
import br.com.valorescorrecoesapi.modules.autenticacao.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/token")
public class AutenticacaoController {

    @Autowired
    private JwtService service;

    @PostMapping
    public JwtResponse autenticarUsuario(@RequestHeader(value="api-secret") String apiSecret,
                                         @RequestBody UsuarioRequest request) {
        return service.gerarTokenAcesso(request);
    }
}
