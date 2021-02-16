package br.com.valorescorrecoesapi.modules.correcoes.controller;

import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoRequest;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoResponse;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoTotaisResponse;
import br.com.valorescorrecoesapi.modules.correcoes.service.CorrecaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/correcoes")
public class CorrecaoController {

    @Autowired
    private CorrecaoService service;

    @PostMapping("salvar")
    public CorrecaoResponse salvarCorrecao(@RequestBody CorrecaoRequest request) {
        return service.salvarCorrecao(request);
    }

    @GetMapping("buscar/ano/{ano}")
    public List<CorrecaoResponse> buscarCorrecoesPorAno(@PathVariable Integer ano) {
        return service.buscarCorrecoesPorAno(ano);
    }

    @GetMapping("buscar/data/{dataCorrecao}")
    public List<CorrecaoResponse> buscarCorrecoesPorData(@PathVariable String dataCorrecao) {
        return service.buscarCorrecoesPorData(dataCorrecao);
    }

    @GetMapping("ano-atual/totais")
    public CorrecaoTotaisResponse buscarTotaisDoAnoAtual() {
        return service.buscarTotaisDoAnoAtual();
    }
}
