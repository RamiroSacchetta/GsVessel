package com.example.GSVessel.Init;

import com.example.GSVessel.Model.Enums.Role;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;

@Component
public class Init {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void inicializarUsuarios() {
        if (!userRepository.existsByEmail("valen6sacchetta@gmail.com")) {
            User user = new User();
            user.setEmail("valen6sacchetta@gmail.com");
            user.setUsername("valen");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(Role.ADMIN);
            user.setEnabled(true);
            userRepository.save(user);
        }
    }



}
