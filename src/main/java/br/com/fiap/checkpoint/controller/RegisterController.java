package br.com.fiap.checkpoint.controller;


import br.com.fiap.checkpoint.dto.BookRequestDTO;
import br.com.fiap.checkpoint.dto.UserRequestDTO;
import br.com.fiap.checkpoint.model.Book;
import br.com.fiap.checkpoint.model.User;
import br.com.fiap.checkpoint.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class RegisterController {

    @Autowired
    private UserService service;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/add")
    private String post(@ModelAttribute("user") @Valid UserRequestDTO dto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("user", dto);
            return "auth/register";
        }
        service.createUser(dto);
        return "redirect:/login";
    }

}
