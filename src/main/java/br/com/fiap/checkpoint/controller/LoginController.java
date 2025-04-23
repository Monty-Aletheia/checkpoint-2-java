package br.com.fiap.checkpoint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loadLoginScreen(){
        return "auth/login";
    }

}


