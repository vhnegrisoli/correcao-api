package br.com.valorescorrecoesapi.modules.correcoes.repository;

import br.com.valorescorrecoesapi.modules.correcoes.enums.EStatusCorrecao;
import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CorrecaoRepository extends JpaRepository<Correcao, Integer> {

    Optional<Correcao> findByIdAndUsuarioIdAndStatus(Integer id, Integer usuarioId, EStatusCorrecao status);

    List<Correcao> findByAnoAndUsuarioIdAndStatus(Integer ano, Integer usuarioId, EStatusCorrecao status);

    Boolean existsByDataCorrecaoAndUsuarioIdAndStatus(LocalDate dataCorrecao, Integer usuarioId, EStatusCorrecao status);

    void deleteByIdAndUsuarioIdAndStatus(Integer id, Integer usuarioId, EStatusCorrecao status);
}
