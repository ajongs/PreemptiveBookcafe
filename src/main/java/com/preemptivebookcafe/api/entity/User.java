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

    private Long class_no;
    private String password;
    private String email;


    public User(Long class_no, String password, String email){
        this.class_no = class_no;
        this.password = password;
        this.email = email;
    }
}
