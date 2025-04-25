package br.com.fiap.checkpoint.service;

import br.com.fiap.checkpoint.model.User;
import br.com.fiap.checkpoint.dto.BookRequestDTO;
import br.com.fiap.checkpoint.dto.UserRequestDTO;
import br.com.fiap.checkpoint.model.Book;
import br.com.fiap.checkpoint.model.User;
import br.com.fiap.checkpoint.model.enums.Role;
import br.com.fiap.checkpoint.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.time.LocalDate;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    public void createUser(UserRequestDTO dto){
        User user = new User(Role.USER, passwordEncoder.encode(dto.password()), dto.email(), dto.name());

        userRepository.save(user);
    }

}
