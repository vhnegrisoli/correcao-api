package br.com.valorescorrecoesapi.modules.correcoes.repository;

import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CorrecaoRepository extends JpaRepository<Correcao, Integer> {

    List<Correcao> findByAno(Integer ano);

    Optional<Correcao> findByDataCorrecao(LocalDate dataCorrecao);

    Boolean existsByDataCorrecao(LocalDate dataCorrecao);

    Boolean existsByDataCorrecaoAndIdNot(LocalDate dataCorrecao, Integer id);
}
