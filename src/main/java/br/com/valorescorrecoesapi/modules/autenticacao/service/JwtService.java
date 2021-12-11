package br.com.valorescorrecoesapi.modules.autenticacao.service;

import br.com.valorescorrecoesapi.config.RequestUtil;
import br.com.valorescorrecoesapi.config.exception.AutenticacaoException;
import br.com.valorescorrecoesapi.config.exception.ValidacaoException;
import br.com.valorescorrecoesapi.modules.autenticacao.dto.JwtResponse;
import br.com.valorescorrecoesapi.modules.autenticacao.dto.UsuarioRequest;
import br.com.valorescorrecoesapi.modules.autenticacao.model.Usuario;
import br.com.valorescorrecoesapi.modules.autenticacao.repository.UsuarioRepository;
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

import static br.com.valorescorrecoesapi.config.Constantes.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class JwtService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${authorization.jwt-secret}")
    private String secret;

    public JwtResponse gerarTokenAcesso(UsuarioRequest request) {
        validarRequestExistente(request);
        validarUsuarioInformado(request);
        var usuarioExistente = buscarPorEmail(request.getUsuario());
        validarSenha(request, usuarioExistente);
        return new JwtResponse(gerarJwt(usuarioExistente));
    }

    private void validarRequestExistente(UsuarioRequest request) {
        if (isEmpty(request)) {
            throw new ValidacaoException("É necessário informar os dados de acesso para gerar um token de acesso.");
        }
    }

    private void validarUsuarioInformado(UsuarioRequest request) {
        if (isEmpty(request.getUsuario())) {
            throw new ValidacaoException("É necessário informar o usuário.");
        }
        if (isEmpty(request.getSenha())) {
            throw new ValidacaoException("É necessário informar a senha.");
        }
    }

    private void validarSenha(UsuarioRequest request, Usuario usuario) {
        if (!validarSenhaCorreta(request, usuario)) {
            throw new AutenticacaoException("Senha incorreta. Tente novamente.");
        }
    }

    private boolean validarSenhaCorreta(UsuarioRequest request, Usuario usuario) {
        return encoder.matches(request.getSenha(), usuario.getSenha());
    }

    private String gerarJwt(Usuario usuario) {
        HashMap<String, String> data = new HashMap<>();

        data.put(USUARIO_ID, usuario.getId().toString());
        data.put(USUARIO_NOME, usuario.getNome());
        data.put(USUARIO_EMAIL, usuario.getEmail());

        return Jwts
            .builder()
            .setSubject(usuario.getNome())
            .setClaims(data)
            .setExpiration(gerarExpiracao())
            .signWith(gerarChaveJwt())
            .compact();
    }

    public void validarUsuarioAutenticado(String token) {
        token = extrairBearerDoToken(token);
        var usuario = (String) descriptografarJwt(token).getBody().get(USUARIO_ID);
        if (isEmpty(usuario)) {
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

    public Usuario obterUsuarioAutenticado() {
        try {
            var token = extrairBearerDoToken(RequestUtil.getCurrentRequest().getHeader("Authorization"));
            var email = (String) descriptografarJwt(token).getBody().get(USUARIO_EMAIL);
            return buscarPorEmail(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ValidacaoException("Não foi possível recuperar o usuário autenticado.");
        }
    }

    private Usuario buscarPorEmail(String email) {
        return usuarioRepository
            .findByEmail(email)
            .orElseThrow(() -> new AutenticacaoException("Usuário incorreto. Tente novamente."));
    }

    private String extrairBearerDoToken(String accessToken) {
        accessToken = accessToken.replace(BEARER, EMPTY);
        accessToken = accessToken.replace(EMPTY_SPACE, EMPTY);
        return accessToken;
    }
}
