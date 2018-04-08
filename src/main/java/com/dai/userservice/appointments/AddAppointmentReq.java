package com.dai.userservice.appointments;

import lombok.*;
import lombok.experimental.Wither;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Wither
public class AddAppointmentReq {

    private String title;

    private String description;

    private String date;

    private String time;

    private String patient;

    private String doctor;


    public boolean validate() {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(description) || StringUtils.isEmpty(date)
                || StringUtils.isEmpty(time) || StringUtils.isEmpty(patient) || StringUtils.isEmpty(doctor)) {
            return false;
        }

        try {
            Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
