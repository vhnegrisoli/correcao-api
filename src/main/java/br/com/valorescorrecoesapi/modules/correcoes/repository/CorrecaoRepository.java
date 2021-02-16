package br.com.valorescorrecoesapi.modules.correcoes.repository;

import br.com.valorescorrecoesapi.modules.correcoes.model.Correcao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CorrecaoRepository extends JpaRepository<Correcao, Integer> {

    List<Correcao> findByAno(Integer ano);

    List<Correcao> findByDataCorrecao(LocalDate dataCorrecao);
}
