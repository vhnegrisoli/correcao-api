package br.com.valorescorrecoesapi.config;

import br.com.valorescorrecoesapi.config.exception.AutenticacaoException;
import br.com.valorescorrecoesapi.modules.autenticacao.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;

import static br.com.valorescorrecoesapi.config.Constantes.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class AuthorizationTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService service;

    @Value("${authorization.api-secret}")
    private String apiSecret;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (isOptions(request)) {
            return true;
        }
        validarHeaderApiSecret(request);
        validarHeaderAuthorization(request);
        return true;
    }

    private void validarHeaderApiSecret(HttpServletRequest request) {
        if (isEndpointNecessarioParaHeaderApiSecret(request)) {
            validarRequestSemApiSecret(request);
            validarApiSecretCorreto(request);
        }
    }

    private void validarRequestSemApiSecret(HttpServletRequest request) {
        if (isEmpty(request.getHeader(API_SECRET_HEADER))) {
            gerarAutenticacaoException("É necessário informar o header ".concat(API_SECRET_HEADER));
        }
    }

    private void validarApiSecretCorreto(HttpServletRequest request) {
        var requestApiSecret = request.getHeader(API_SECRET_HEADER);
        var base64RequestApiSecret = Base64.getEncoder().encode(requestApiSecret.getBytes());
        if (!Arrays.equals(base64RequestApiSecret, apiSecret.getBytes())) {
            gerarAutenticacaoException("Header api-secret inválido.");
        }
    }

    private void validarHeaderAuthorization(HttpServletRequest request) {
        if (isEndpointNecessarioParaHeaderAuthorization(request)) {
            validarRequestSemHeaderAuthorization(request);
            service.validarUsuarioAutenticado(request.getHeader(AUTHORIZATION_HEADER));
        }
    }

    private void validarRequestSemHeaderAuthorization(HttpServletRequest request) {
        if (isEmpty(request.getHeader(AUTHORIZATION_HEADER))) {
            gerarAutenticacaoException("Não autenticado. Informe um token de acesso.");
        }
    }

    private void gerarAutenticacaoException(String message) {
        throw new AutenticacaoException(message);
    }

    private boolean isEndpointNecessarioParaHeaderApiSecret(HttpServletRequest request) {
        return request.getRequestURI().contains(CORRECAO_ENDPOINT) || request.getRequestURI().contains(TOKEN_ENDPOINT);
    }

    private boolean isEndpointNecessarioParaHeaderAuthorization(HttpServletRequest request) {
        return request.getRequestURI().contains(CORRECAO_ENDPOINT);
    }

    private boolean isOptions(HttpServletRequest request) {
        return request.getMethod().equals(OPTIONS_METHOD);
    }
}
