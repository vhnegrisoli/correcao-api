package br.com.valorescorrecoesapi.modules.autenticacao.service;

import br.com.valorescorrecoesapi.config.exception.AutenticacaoException;
import br.com.valorescorrecoesapi.config.exception.ValidacaoException;
import br.com.valorescorrecoesapi.modules.autenticacao.dto.ApiUser;
import br.com.valorescorrecoesapi.modules.autenticacao.dto.JwtResponse;
import br.com.valorescorrecoesapi.modules.autenticacao.dto.UsuarioRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import static br.com.valorescorrecoesapi.config.Constantes.DEZ_MINUTOS;
import static br.com.valorescorrecoesapi.config.Constantes.USUARIO;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class JwtService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ApiUser apiUser;

    @Value("${authorization.jwt-secret}")
    private String secret;

    public JwtResponse gerarTokenAcesso(UsuarioRequest request) {
        validarUsuarioSenha(request);
        return new JwtResponse(gerarJwt());
    }

    private void validarUsuarioSenha(UsuarioRequest request) {
        validarRequestExistente(request);
        validarUsuario(request);
        validarSenha(request);
    }

    private void validarRequestExistente(UsuarioRequest request) {
        if (isEmpty(request)) {
            throw new ValidacaoException("É necessário informar os dados de acesso para gerar um token de acesso.");
        }
    }

    private void validarUsuario(UsuarioRequest request) {
        if (isEmpty(request.getUsuario())) {
            throw new ValidacaoException("É necessário informar o usuário.");
        }
        if (!request.getUsuario().equals(apiUser.getUser())) {
            throw new AutenticacaoException("Usuário incorreto. Tente novamente.");
        }
    }

    private void validarSenha(UsuarioRequest request) {
        if (isEmpty(request.getSenha())) {
            throw new ValidacaoException("É necessário informar a senha.");
        }
        if (!validarSenhaCorreta(request)) {
            throw new AutenticacaoException("Senha incorreta. Tente novamente.");
        }
    }

    private boolean validarSenhaCorreta(UsuarioRequest request) {
        return encoder.matches(request.getSenha(), apiUser.getPassword());
    }

    private String gerarJwt() {
        HashMap<String, String> data = new HashMap<>();

        data.put(USUARIO, apiUser.getUser());

        return Jwts
            .builder()
            .setSubject(apiUser.getUser())
            .setClaims(data)
            .setExpiration(gerarExpiracao())
            .signWith(gerarChaveJwt())
            .compact();
    }

    public void validarUsuarioAutenticado(String token) {
        var usuario = (String) descriptografarJwt(token).getBody().get(USUARIO);
        if (isEmpty(usuario) || !isEmpty(usuario) && !apiUser.getUser().equals(usuario)) {
            throw new AutenticacaoException("Erro. Usuário ou senha inválidos.");
        }
    }

    private Jws<Claims> descriptografarJwt(String jwt) {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(gerarChaveJwt())
                .build()
                .parseClaimsJws(jwt);
        } catch (Exception ex) {
            throw new AutenticacaoException("Token de acesso inválido.");
        }
    }

    private SecretKey gerarChaveJwt() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Date gerarExpiracao() {
        return Date.from(
            LocalDateTime.now()
                .plusMinutes(DEZ_MINUTOS)
                .atZone(ZoneId.systemDefault()).toInstant()
        );
    }
}
