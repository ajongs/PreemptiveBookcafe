package com.preemptivebookcafe.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private Long classNo;
    private String password;
    private String email;


    private String salt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull()
    private Boolean isDeleted;

    @PrePersist
    public void setDefaultValue(){
        if(isDeleted == null){
            isDeleted = false;
        }
        if(createdAt==null){
            createdAt = LocalDateTime.now();
        }
    }

    public User(Long classNo, String password, String email, String salt){
        this.classNo = classNo;
        this.password = password;
        this.email = email;
        this.salt = salt;
    }
}
