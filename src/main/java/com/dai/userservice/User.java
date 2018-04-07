package com.dai.userservice;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class User implements Serializable {

    @NotBlank
    @Column(name = "user_name")
    @Id
    private String userName;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String role;

}