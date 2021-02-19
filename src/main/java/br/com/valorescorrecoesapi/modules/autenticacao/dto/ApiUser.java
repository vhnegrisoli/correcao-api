package br.com.valorescorrecoesapi.modules.autenticacao.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("authorization.user")
public class ApiUser {

    private String user;
    private String password;
}
