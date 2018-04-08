package com.dai.userservice.appointments;

import com.dai.userservice.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    public List<Appointment> findAllByPatient(User patient);

    public List<Appointment> findAllByDoctor(User doctor);
}
