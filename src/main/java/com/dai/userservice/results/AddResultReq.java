package com.dai.userservice.results;

import lombok.*;
import lombok.experimental.Wither;

@AllArgsConstructor
@NoArgsConstructor
@Wither
@Getter
public class AddResultReq {

    private String title;

    private String description;

    private String name;

}
