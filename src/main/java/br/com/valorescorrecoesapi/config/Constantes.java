package br.com.valorescorrecoesapi.config;

public interface Constantes {

    String FORMATO_LOCAL_DATE = "yyyy-MM-dd";
    String FORMATO_RETORNO_DATA = "dd/MM/yyyy";
    Integer TOTAL_CORRECOES_POR_DIA = 200;
    Integer DUAS_CASAS_DECIMAIS = 2;
    Integer ZERO = 0;
    Integer DEZ_MINUTOS = 10;
    String USUARIO_ID = "id";
    String USUARIO_NOME = "nome";
    String USUARIO_EMAIL = "email";
    String API_SECRET_HEADER = "api-secret";
    String AUTHORIZATION_HEADER = "authorization";
    String BEARER = "bearer";
    String EMPTY_SPACE = " ";
    String EMPTY = "";
    String TOKEN_ENDPOINT = "api/auth/token";
    String CORRECAO_ENDPOINT = "api/correcoes";
    String OPTIONS_METHOD = "OPTIONS";
}
