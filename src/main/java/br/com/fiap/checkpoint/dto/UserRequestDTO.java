package br.com.fiap.checkpoint.dto;

import br.com.fiap.checkpoint.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(

        @NotBlank
        String name,

        @NotBlank
        String email,

        @NotBlank
        String password

) {



}
