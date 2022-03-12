package com.preemptivebookcafe.repository.user;

import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional //생략하지 말자...
//@Rollback(false)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void create(){
        User user = new User(201601821L, "1234", "w@naver.com");
        userRepository.save(user);

        User user2 = userRepository.findById(user.getId()).get();
        System.out.println("user2.getClass_no() = " + user2.getClass_no());
        System.out.println("user2.getPassword() = " + user2.getPassword());
        System.out.println("user2.getEmail() = " + user2.getEmail());
        assertThat(user2).isEqualTo(user);

    }
}
