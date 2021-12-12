package br.com.valorescorrecoesapi.modules.correcoes.controller;

import br.com.valorescorrecoesapi.modules.correcoes.dto.*;
import br.com.valorescorrecoesapi.modules.correcoes.enums.EProcessoSeletivo;
import br.com.valorescorrecoesapi.modules.correcoes.service.CorrecaoService;
import br.com.valorescorrecoesapi.modules.correcoes.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/historico")
public class HistoricoController {

    @Autowired
    private HistoricoService service;

    @GetMapping("disponiveis")
    public List<CorrecaoHistoricoResponse> buscarHistoricosDasCorrecoes(@RequestHeader(value="api-secret") String apiSecret,
                                                                        @RequestHeader(value="authorization") String authorization) {
        return service.buscarHistoricosDasCorrecoes();
    }

    @GetMapping("processo/{processoSeletivo}")
    public List<CorrecaoResponse> buscarCorrecoesFechadasPorProcesso(@RequestHeader(value="api-secret") String apiSecret,
                                                                     @RequestHeader(value="authorization") String authorization,
                                                                     @PathVariable EProcessoSeletivo processoSeletivo) {
        return service.buscarCorrecoesPorProcessoSeletivo(processoSeletivo);
    }
    @GetMapping("totais/{processoSeletivo}")
    public CorrecaoTotaisResponse buscarTotaisDoAnoAtual(@RequestHeader(value="api-secret") String apiSecret,
                                                         @RequestHeader(value="authorization") String authorization,
                                                         @PathVariable EProcessoSeletivo processoSeletivo) {
        return service.buscarTotais(processoSeletivo);
    }

    @GetMapping("diarias/{processoSeletivo}")
    public List<CorrecoesDiarias> buscarCorrecoesPorDia(@RequestHeader(value="api-secret") String apiSecret,
                                                        @RequestHeader(value="authorization") String authorization,
                                                        @PathVariable EProcessoSeletivo processoSeletivo) {
        return service.buscarCorrecoesPorDia(processoSeletivo);
    }

    @GetMapping("por-tipo/{processoSeletivo}")
    public CorrecoesPorTipo buscarCorrecoesPorTipo(@RequestHeader(value="api-secret") String apiSecret,
                                                   @RequestHeader(value="authorization") String authorization,
                                                   @PathVariable EProcessoSeletivo processoSeletivo) {
        return service.buscarCorrecoesPorTipo(processoSeletivo);
    }
}
