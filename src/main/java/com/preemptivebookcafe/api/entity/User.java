package com.preemptivebookcafe.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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


    public User(Long classNo, String password, String email){
        this.classNo = classNo;
        this.password = password;
        this.email = email;
    }
}
