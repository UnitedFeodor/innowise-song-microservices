package com.innowise.authapi.repository;

import com.innowise.authapi.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User,Integer> {

    Optional<User> findUserByUsername(String username);
}
