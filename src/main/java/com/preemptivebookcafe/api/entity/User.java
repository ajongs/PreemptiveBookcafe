package com.preemptivebookcafe.api.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class User {

    @Id
    @Column(name = "user_id")
    private Long class_no;
    private String password;
    private String email;
}
