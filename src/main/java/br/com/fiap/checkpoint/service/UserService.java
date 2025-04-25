package br.com.fiap.checkpoint.service;

import br.com.fiap.checkpoint.model.User;
import br.com.fiap.checkpoint.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
