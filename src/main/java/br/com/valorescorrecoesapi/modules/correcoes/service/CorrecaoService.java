package br.com.valorescorrecoesapi.modules.correcoes.service;

import br.com.valorescorrecoesapi.config.exception.ValidacaoException;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoRequest;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoResponse;
import br.com.valorescorrecoesapi.modules.correcoes.dto.CorrecaoTotaisResponse;
import br.com.valorescorrecoesapi.modules.correcoes.enums.ETipoCorrecao;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import br.com.valorescorrecoesapi.modules.correcoes.repository.CorrecaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.valorescorrecoesapi.config.exception.Constantes.FORMATO_LOCAL_DATE;
import static br.com.valorescorrecoesapi.config.exception.Constantes.TOTAL_CORRECOES_POR_DIA;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class CorrecaoService {

    @Autowired
    private CorrecaoRepository repository;

    @Transactional
    public CorrecaoResponse salvarCorrecao(CorrecaoRequest request) {
        validarDadosCorrecao(request);
        var correcao = Correcao.gerarCorrecao(request);
        repository.save(correcao);
        return CorrecaoResponse.gerar(correcao);
    }

    private void validarDadosCorrecao(CorrecaoRequest request) {
        validarCorrecaoComTipo(request);
        validarCorrecaoComData(request);
        validarCorrecaoComTotalCorrigido(request);
        validarTotalMaiorQue100(request);
    }

    private void validarCorrecaoComTipo(CorrecaoRequest request) {
        if (isEmpty(request.getTipoCorrecao())) {
            throw new ValidacaoException("É necessário informar o tipo da correção. Opções: "
                .concat(Arrays.toString(ETipoCorrecao.values())));
        }
    }

    private void validarCorrecaoComData(CorrecaoRequest request) {
        if (isEmpty(request.getDataCorrecao())) {
            throw new ValidacaoException("É necessário informar a data da correção no formato: "
                .concat(FORMATO_LOCAL_DATE));
        }
    }

    private void validarCorrecaoComTotalCorrigido(CorrecaoRequest request) {
        if (isEmpty(request.getTotalCorrigido())) {
            throw new ValidacaoException("É necessário informar o total corrigido.");
        }
    }

    private void validarTotalMaiorQue100(CorrecaoRequest request) {
        if (request.getTotalCorrigido() > TOTAL_CORRECOES_POR_DIA) {
            throw new ValidacaoException("O total de correções por dia é 100.");
        }
    }

    public List<CorrecaoResponse> buscarCorrecoesPorAno(Integer ano) {
        if (isEmpty(ano)) {
            return Collections.emptyList();
        }
        return repository
            .findByAno(ano)
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
