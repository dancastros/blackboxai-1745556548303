package com.hortensia.repository;

import com.hortensia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDni(String dni);
    Optional<User> findByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}
