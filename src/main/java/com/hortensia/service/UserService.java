package com.hortensia.service;

import com.hortensia.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByDni(String dni);
    Optional<User> getUserByEmail(String email);
    User saveUser(User user);
    void deleteUser(Long id);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
    User updateUser(Long id, User userDetails);
}
