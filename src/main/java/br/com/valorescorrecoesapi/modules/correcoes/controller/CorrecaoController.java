package br.com.valorescorrecoesapi.modules.correcoes.controller;

import br.com.valorescorrecoesapi.modules.correcoes.dto.*;
import br.com.valorescorrecoesapi.modules.correcoes.service.CorrecaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/correcoes")
public class CorrecaoController {

    @Autowired
    private CorrecaoService service;

    @PostMapping
    public CorrecaoResponse salvarCorrecao(@RequestHeader(value="api-secret") String apiSecret,
                                           @RequestHeader(value="authorization") String authorization,
                                           @RequestBody CorrecaoRequest request) {
        return service.salvarCorrecao(request);
    }

    @DeleteMapping("{id}")
    public void removerCorrecao(@RequestHeader(value="api-secret") String apiSecret,
                                @RequestHeader(value="authorization") String authorization,
                                @PathVariable Integer id) {
        service.removerCorrecao(id);
    }

    @GetMapping
    public List<CorrecaoResponse> buscarCorrecoes(@RequestHeader(value="api-secret") String apiSecret,
                                                  @RequestHeader(value="authorization") String authorization) {
        return service.buscarCorrecoes();
    }

    @GetMapping("totais")
    public CorrecaoTotaisResponse buscarTotaisDoAnoAtual(@RequestHeader(value="api-secret") String apiSecret,
                                                         @RequestHeader(value="authorization") String authorization) {
        return service.buscarTotais();
    }

    @GetMapping("diarias")
    public List<CorrecoesDiarias> buscarCorrecoesPorDia(@RequestHeader(value="api-secret") String apiSecret,
                                                        @RequestHeader(value="authorization") String authorization) {
        return service.buscarCorrecoesPorDia();
    }

    @GetMapping("por-tipo")
    public CorrecoesPorTipo buscarCorrecoesPorTipo(@RequestHeader(value="api-secret") String apiSecret,
                                                   @RequestHeader(value="authorization") String authorization) {
        return service.buscarCorrecoesPorTipo();
    }
}
