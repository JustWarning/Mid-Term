package com.midterm.service.framework;

import com.midterm.dto.UserDto;
import com.midterm.entity.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService{
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void save(User user);

    void deleteUser(Long id);
}

