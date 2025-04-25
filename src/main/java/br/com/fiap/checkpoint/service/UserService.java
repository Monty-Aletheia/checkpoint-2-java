package br.com.fiap.checkpoint.service;

import br.com.fiap.checkpoint.dto.UserRequestDTO;
import br.com.fiap.checkpoint.model.User;
import br.com.fiap.checkpoint.model.enums.Role;
import br.com.fiap.checkpoint.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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
    private final MeterRegistry meterRegistry;

    private final Counter usersCreatedCounter;
    private final Timer userCreationTimer;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MeterRegistry meterRegistry) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.meterRegistry = meterRegistry;

        this.usersCreatedCounter = Counter.builder("users_created_total")
                .description("Total de usuários criados com sucesso")
                .register(meterRegistry);

        this.userCreationTimer = Timer.builder("user.creation.time")
                .description("Tempo gasto para criar um usuário")
                .register(meterRegistry);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void createUser(UserRequestDTO dto) {
        Timer.Sample sample = Timer.start(meterRegistry);

        User user = new User(Role.USER, passwordEncoder.encode(dto.password()), dto.email(), dto.name());
        userRepository.save(user);

        sample.stop(userCreationTimer);
        usersCreatedCounter.increment();
    }
}
