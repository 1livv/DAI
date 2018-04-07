package com.dai.userservice;

import com.dai.userservice.results.Result;
import lombok.*;
import lombok.experimental.Wither;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Wither
public class MyResponse {

    private String message;

    private List<Result> results;
}
