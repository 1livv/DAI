package com.dai.userservice.appointments;

import com.dai.userservice.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("Select t from appointments  t where t.patient.name=:patient")
    public List<Appointment> findAllByPatient( @Param("patient") String patient);

    @Query("Select t from appointments  t where t.doctor.name=:doctor")
    public List<Appointment> findAllByDoctor(@Param("doctor") String doctor);
}
