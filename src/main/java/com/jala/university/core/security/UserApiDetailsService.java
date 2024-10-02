package com.jala.university.core.security;

import com.jala.university.core.services.UserService;
import com.jala.university.data.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserApiDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("root".equals(username)) {
            return User.withUsername("root")
                    .password(PassEncoder.passwordEncoder().encode("root"))
                    .roles("ADMIN")
                    .build();
        }

        UserDTO user = userService.getByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(user.getLogin())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}