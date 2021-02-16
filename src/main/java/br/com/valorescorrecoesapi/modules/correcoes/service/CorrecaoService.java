package br.com.valorescorrecoesapi.modules.correcoes.service;

import br.com.valorescorrecoesapi.config.exception.ValidacaoException;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoRequest;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoResponse;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoTotaisResponse;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import br.com.valorescorrecoesapi.modules.correcoes.repository.CorrecaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.valorescorrecoesapi.config.Constantes.FORMATO_LOCAL_DATE;
import static br.com.valorescorrecoesapi.config.Constantes.TOTAL_CORRECOES_POR_DIA;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class CorrecaoService {

    @Autowired
    private CorrecaoRepository repository;

    @Transactional
    public CorrecaoResponse salvarCorrecao(CorrecaoRequest request) {
        validarDadosCorrecao(request, null);
        var correcao = Correcao.gerarCorrecao(request);
        repository.save(correcao);
        return CorrecaoResponse.gerar(correcao);
    }

    @Transactional
    public CorrecaoResponse editarCorrecao(Integer id, CorrecaoRequest request) {
        validarDadosCorrecao(request, id);
        var correcao = Correcao.gerarCorrecao(request);
        correcao.setId(id);
        repository.save(correcao);
        return CorrecaoResponse.gerar(correcao);
    }

    @Transactional
    public void removerCorrecao(Integer id) {
        repository.deleteById(id);
    }

    private void validarDadosCorrecao(CorrecaoRequest request, Integer id) {
        validarCorrecaoComData(request);
        validarTotalMaiorQue100(request);
        validarDataJaExistente(request, id);
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

    private void validarDataJaExistente(CorrecaoRequest request, Integer id) {
        if (isEmpty(id) && repository.existsByDataCorrecao(request.getDataCorrecao())) {
            throw new ValidacaoException("Não é possível salvar esta correção "
                + "pois já foi registrada uma correção para esta data.");
        }
        if (repository.existsByDataCorrecaoAndIdNot(request.getDataCorrecao(), id)) {
            throw new ValidacaoException("Não é possível editar esta correção "
                + "pois já foi registrada uma correção para esta data.");
        }
    }

    public List<CorrecaoResponse> buscarCorrecoesPorAno(Integer ano) {
        return repository
            .findByAno(isEmpty(ano) ? LocalDate.now().getYear() : ano)
            .stream()
            .map(CorrecaoResponse::gerar)
            .collect(Collectors.toList());
    }

    public List<CorrecaoResponse> buscarCorrecoesPorData(String dataCorrecao) {
        return repository
            .findByDataCorrecao(converterStringDataEmLocalDate(dataCorrecao))
            .stream()
            .map(CorrecaoResponse::gerar)
            .collect(Collectors.toList());
    }

    private LocalDate converterStringDataEmLocalDate(String data) {
        try {
            return LocalDate.parse(data);
        } catch (Exception ex) {
            log.error("Erro ao tentar converter data: {}. Erro: {}", data, ex);
            throw new ValidacaoException("Formato de data inválido.");
        }
    }

    public CorrecaoTotaisResponse buscarTotaisDoAnoAtual() {
        return CorrecaoTotaisResponse.gerar(repository.findByAno(LocalDate.now().getYear()));
    }
}
