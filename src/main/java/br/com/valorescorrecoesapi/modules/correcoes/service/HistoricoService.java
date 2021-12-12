package br.com.valorescorrecoesapi.modules.correcoes.service;

import br.com.valorescorrecoesapi.modules.autenticacao.service.JwtService;
import br.com.valorescorrecoesapi.modules.correcoes.dto.*;
import br.com.valorescorrecoesapi.modules.correcoes.enums.EProcessoSeletivo;
import br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import br.com.valorescorrecoesapi.modules.correcoes.repository.CorrecaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.valorescorrecoesapi.config.Constantes.ZERO;
import static br.com.valorescorrecoesapi.modules.correcoes.enums.EStatusCorrecao.ABERTA;
import static br.com.valorescorrecoesapi.modules.correcoes.enums.EStatusCorrecao.FECHADA;
import static br.com.valorescorrecoesapi.modules.correcoes.util.NumeroUtil.converterParaDuasCasasDecimais;

@Slf4j
@Service
public class HistoricoService {

    @Autowired
    private CorrecaoRepository repository;

    @Autowired
    private JwtService jwtService;

    public List<CorrecaoHistoricoResponse> buscarHistoricosDasCorrecoes() {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        return repository
            .findByUsuarioIdAndStatus(usuarioId, FECHADA)
            .stream()
            .map(CorrecaoHistoricoResponse::gerar)
            .distinct()
            .collect(Collectors.toList());
    }

    public List<CorrecaoResponse> buscarCorrecoesPorProcessoSeletivo(EProcessoSeletivo processoSeletivo) {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        return repository
            .findByUsuarioIdAndStatusAndProcessoSeletivo(usuarioId, FECHADA, processoSeletivo)
            .stream()
            .map(CorrecaoResponse::gerar)
            .sorted(Comparator.comparing(CorrecaoResponse::getDataCorrecao))
            .collect(Collectors.toList());
    }

    public CorrecaoTotaisResponse buscarTotais(EProcessoSeletivo processoSeletivo) {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        return CorrecaoTotaisResponse.gerar(repository
            .findByUsuarioIdAndStatusAndProcessoSeletivo(usuarioId, FECHADA, processoSeletivo));
    }

    public List<CorrecoesDiarias> buscarCorrecoesPorDia(EProcessoSeletivo processoSeletivo) {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        var correcoes = repository.findByUsuarioIdAndStatusAndProcessoSeletivo(usuarioId, FECHADA, processoSeletivo);
        return correcoes
            .stream()
            .map(CorrecoesDiarias::converterDe)
            .collect(Collectors.toList());
    }

    public CorrecoesPorTipo buscarCorrecoesPorTipo(EProcessoSeletivo processoSeletivo) {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        var correcoes = repository.findByUsuarioIdAndStatusAndProcessoSeletivo(usuarioId, FECHADA, processoSeletivo);

        var normais = converterParaDuasCasasDecimais(correcoes
            .stream()
            .map(Correcao::getQtdNormal)
            .reduce(ZERO, Integer::sum) * ETipoCorrecao.NORMAL.getValorCorrecao().doubleValue());

        var terceirasCorrecoes = converterParaDuasCasasDecimais(correcoes
            .stream()
            .map(Correcao::getQtdTerceiraCorrecao)
            .reduce(ZERO, Integer::sum) * ETipoCorrecao.TERCEIRA_CORRECAO.getValorCorrecao().doubleValue());

        var avaliacoesDesempenho = converterParaDuasCasasDecimais(correcoes
            .stream()
            .map(Correcao::getQtdAvaliacaoDesempenho)
            .reduce(ZERO, Integer::sum) * ETipoCorrecao.AVALIACAO_DESEMPENHO.getValorCorrecao().doubleValue());

        return new CorrecoesPorTipo(normais, terceirasCorrecoes, avaliacoesDesempenho);
    }
}
