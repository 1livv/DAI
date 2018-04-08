package com.dai.userservice.results;

import com.dai.userservice.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity(name = "results")
@Table(name = "results")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "name", referencedColumnName = "name")
    @JsonIgnore
    private User user;
}
