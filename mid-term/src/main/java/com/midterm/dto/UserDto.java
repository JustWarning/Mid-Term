package com.midterm.dto;

import lombok.Data;

import java.util.Set;
import com.midterm.dto.RoleDto;

@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<RoleDto> roles;
}
