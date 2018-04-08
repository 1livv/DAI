package com.dai.userservice.appointments;

import com.dai.userservice.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Wither;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Entity
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Wither
public class Appointment {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Date date;

    @NotBlank
    private String time;


    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "name")
    @JsonIgnore
    private User patient;

    @JsonProperty("patient")
    private String getPatientName() {
        return patient.getName();
    }

    @JsonProperty("doctor")
    private String getDoctorName() {
        return doctor.getName();
    }

    @ManyToOne
    @JoinColumn(name = "doctor", referencedColumnName = "name")
    @JsonIgnore
    private User doctor;


    public Appointment(AddAppointmentReq req, User patient, User doctor) {
        this.date = Date.valueOf(req.getDate());
        this.title = req.getTitle();
        this.description = req.getDescription();
        this.time = req.getTime();
        this.doctor = doctor;
        this.patient = patient;

    }



}
