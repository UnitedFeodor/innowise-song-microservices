package com.innowise.authapi.repository.initializer;

import com.innowise.authapi.model.User;
import com.innowise.authapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDataLoader implements ApplicationRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;



    public void run(ApplicationArguments args) {
        final String usernameUser = "user";
        Optional<User> optionalUser = userRepo.findUserByUsername(usernameUser);

        optionalUser.ifPresentOrElse(userValue -> {},
                () -> {
                    User newUser = User.builder()
                            .username(usernameUser)
                            .password(passwordEncoder.encode("user"))
                            .role("USER")
                            .build();
                    userRepo.save(newUser);
                }
        );

        String usernameAdmin = "admin";
        optionalUser = userRepo.findUserByUsername(usernameAdmin);
        optionalUser.ifPresentOrElse(userValue -> {},
                () -> {
                    User newUser = User.builder()
                            .username(usernameAdmin)
                            .password(passwordEncoder.encode("admin"))
                            .role("ADMIN")
                            .build();
                    userRepo.save(newUser);
                }
        );
    }
}
