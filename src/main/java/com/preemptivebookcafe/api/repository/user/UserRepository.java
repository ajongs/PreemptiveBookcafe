package com.preemptivebookcafe.api.repository.user;

import com.preemptivebookcafe.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByClassNo(Long classNo);
}
