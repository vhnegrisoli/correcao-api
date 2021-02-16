package br.com.valorescorrecoesapi.modules.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String redirecionarParaSwagger() {
        return "redirect:swagger-ui.html";
    }
}
