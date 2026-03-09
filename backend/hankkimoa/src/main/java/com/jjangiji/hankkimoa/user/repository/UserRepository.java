package com.jjangiji.hankkimoa.user.repository;


import com.jjangiji.hankkimoa.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
