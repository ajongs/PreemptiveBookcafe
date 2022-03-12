package com.preemptivebookcafe.api.repository.user;

import com.preemptivebookcafe.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
