package br.com.valorescorrecoesapi.modules.correcoes.service;

import br.com.valorescorrecoesapi.config.exception.ValidacaoException;
import br.com.valorescorrecoesapi.modules.autenticacao.service.JwtService;
import br.com.valorescorrecoesapi.modules.correcoes.dto.*;
import br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import br.com.valorescorrecoesapi.modules.correcoes.repository.CorrecaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.valorescorrecoesapi.config.Constantes.*;
import static br.com.valorescorrecoesapi.modules.correcoes.enums.EStatusCorrecao.ABERTA;
import static br.com.valorescorrecoesapi.modules.correcoes.util.NumeroUtil.converterParaDuasCasasDecimais;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class CorrecaoService {

    @Autowired
    private CorrecaoRepository repository;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public CorrecaoResponse salvarCorrecao(CorrecaoRequest request) {
        var usuarioAutenticado = jwtService.obterUsuarioAutenticado();
        validarDadosCorrecao(request, usuarioAutenticado.getId());
        var correcao = Correcao.gerarCorrecao(request, usuarioAutenticado);
        repository.save(correcao);
        return CorrecaoResponse.gerar(correcao);
    }

    @Transactional
    public void removerCorrecao(Integer id) {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        var correcao = buscarCorrecaoPorId(id);
        repository.deleteByIdAndUsuarioIdAndStatus(correcao.getId(), usuarioId, ABERTA);
    }

    private void validarDadosCorrecao(CorrecaoRequest request, Integer usuarioId) {
        validarCorrecaoComData(request);
        validarTotalMaiorQue100(request);
        validarDataJaExistente(request, usuarioId);
    }

    private void validarCorrecaoComData(CorrecaoRequest request) {
        if (isEmpty(request.getDataCorrecao())) {
            throw new ValidacaoException("É necessário informar a data da correção no formato: "
                .concat(FORMATO_LOCAL_DATE));
        }
    }

    private void validarTotalMaiorQue100(CorrecaoRequest request) {
        var total = request.getQtdNormal() + request.getQtdTerceiraCorrecao() + request.getQtdAvaliacaoDesempenho();
        if (total > TOTAL_CORRECOES_POR_DIA) {
            throw new ValidacaoException("O total de correções por dia é 100.");
        }
    }

    private void validarDataJaExistente(CorrecaoRequest request, Integer usuarioId) {
        if (repository.existsByDataCorrecaoAndUsuarioIdAndStatus(request.getDataCorrecao(), usuarioId, ABERTA)) {
            throw new ValidacaoException("Não é possível salvar esta correção "
                + "pois já foi registrada uma correção para esta data.");
        }
    }

    public List<CorrecaoResponse> buscarCorrecoesPorAno(Integer ano) {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        return repository
            .findByAnoAndUsuarioIdAndStatus(isEmpty(ano) ? LocalDate.now().getYear() : ano, usuarioId, ABERTA)
            .stream()
            .map(CorrecaoResponse::gerar)
            .sorted(Comparator.comparing(CorrecaoResponse::getDataCorrecao))
            .collect(Collectors.toList());
    }

    private Correcao buscarCorrecaoPorId(Integer id) {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        return repository
            .findByIdAndUsuarioIdAndStatus(id, usuarioId, ABERTA)
            .orElseThrow(() -> new ValidacaoException("Não foi encontrada nenhuma correção para este ID ou usuário."));
    }

    public CorrecaoTotaisResponse buscarTotaisDoAnoAtual(Integer ano) {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        return CorrecaoTotaisResponse.gerar(repository
            .findByAnoAndUsuarioIdAndStatus(isEmpty(ano) ? LocalDate.now().getYear() : ano, usuarioId, ABERTA));
    }

    public List<CorrecoesDiarias> buscarCorrecoesPorDia() {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        var correcoes = repository.findByAnoAndUsuarioIdAndStatus(LocalDate.now().getYear(), usuarioId, ABERTA);
        return correcoes
            .stream()
            .map(CorrecoesDiarias::converterDe)
            .collect(Collectors.toList());
    }

    public CorrecoesPorTipo buscarCorrecoesPorTipo() {
        var usuarioId = jwtService.obterUsuarioAutenticado().getId();
        var correcoes = repository.findByAnoAndUsuarioIdAndStatus(LocalDate.now().getYear(), usuarioId, ABERTA);

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
