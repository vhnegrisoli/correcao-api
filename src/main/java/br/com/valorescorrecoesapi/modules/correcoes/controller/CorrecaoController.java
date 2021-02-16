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

    @PostMapping
    public CorrecaoResponse salvarCorrecao(@RequestBody CorrecaoRequest request) {
        return service.salvarCorrecao(request);
    }

    @PutMapping("{id}")
    public CorrecaoResponse editarCorrecao(@PathVariable Integer id,
                                           @RequestBody CorrecaoRequest request) {
        return service.editarCorrecao(id, request);
    }

    @DeleteMapping("{id}")
    public void removerCorrecao(@PathVariable Integer id) {
        service.removerCorrecao(id);
    }

    @GetMapping
    public List<CorrecaoResponse> buscarCorrecoes(@RequestParam(required = false) Integer ano) {
        return service.buscarCorrecoesPorAno(ano);
    }

    @GetMapping("buscar/data/{dataCorrecao}")
    public List<CorrecaoResponse> buscarCorrecoesPorData(@PathVariable String dataCorrecao) {
        return service.buscarCorrecoesPorData(dataCorrecao);
    }

    @GetMapping("totais")
    public CorrecaoTotaisResponse buscarTotaisDoAnoAtual() {
        return service.buscarTotaisDoAnoAtual();
    }
}
