package com.midterm.dto;

import lombok.Data;
import java.util.Set;
// Пароль я не включу в дто
@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<RoleDto> roles;
}
