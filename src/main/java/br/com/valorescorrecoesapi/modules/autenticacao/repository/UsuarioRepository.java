package br.com.valorescorrecoesapi.modules.autenticacao.repository;

import br.com.valorescorrecoesapi.modules.autenticacao.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);
}
