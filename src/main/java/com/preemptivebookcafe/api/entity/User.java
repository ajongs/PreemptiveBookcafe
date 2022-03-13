package com.preemptivebookcafe.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private Long classNo;
    private String password;
    private String email;


    private String salt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;


    public User(Long classNo, String password, String email, String salt){
        this.classNo = classNo;
        this.password = password;
        this.email = email;
        this.salt = salt;
    }
}
