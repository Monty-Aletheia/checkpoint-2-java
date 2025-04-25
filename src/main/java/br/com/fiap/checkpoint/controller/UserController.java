package br.com.fiap.checkpoint.controller;

import br.com.fiap.checkpoint.model.User;
import br.com.fiap.checkpoint.model.enums.Role;
import br.com.fiap.checkpoint.repository.UserRepository;
import br.com.fiap.checkpoint.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    private String get(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model){
        userService.delete(id);
        return "redirect:/users/list";
    }

    @PostMapping("/changeRole")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeUserRole(@RequestParam Long userId, @RequestParam String role, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(Role.valueOf(role));
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Perfil do usuário alterado para " + role + " com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuário não encontrado.");
        }
        return "redirect:/users/list";
    }

}
