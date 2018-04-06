package com.dai.userservice;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class User {

    @NotBlank
    @Column(name = "user_name")
    @Id
    private String userName;

    @NotBlank
    private String password;
}
