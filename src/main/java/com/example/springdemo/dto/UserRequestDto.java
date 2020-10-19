package com.example.springdemo.dto;

import com.example.springdemo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    private int id;
    @NotBlank(message = "name dose not required!!!")
    private String name;
    @NotBlank
    private String surname;
    @Email(message = "Mail dose not valid!!!")
    private String username;
    @Size(min = (6))
    private String password;
    @Size(min = (6))
    private String confirmPassword;
    private Role role = Role.USER;

}
