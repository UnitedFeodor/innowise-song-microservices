package com.innowise.authapi.repository.initializer;

import com.innowise.authapi.model.User;
import com.innowise.authapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataLoader implements ApplicationRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;



    public void run(ApplicationArguments args) {
        userRepo.save(
                User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user"))
                        .role("USER")
                        .build()
        );
        userRepo.save(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role("ADMIN")
                        .build()
        );
    }
}
