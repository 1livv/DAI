package com.dai.userservice.appointments;

import com.dai.userservice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/appointments/create")
    public ResponseEntity<MyResponse> addAppointment(@RequestBody AddAppointmentReq req) {
        if (!req.validate()) {
            return new ResponseEntity<>(new MyResponse().withMessage("Invalid fields"), HttpStatus.BAD_REQUEST);
        }

        List<User> doctors = userRepository.findAllByName(req.getDoctor());
        List<User> patients = userRepository.findAllByName(req.getPatient());

        if (doctors != null && doctors.isEmpty()) {
            return new ResponseEntity<>(new MyResponse().withMessage("The doctor does not exists"),
                    HttpStatus.BAD_REQUEST);
        }

        if (patients != null && patients.isEmpty()) {
            return new ResponseEntity<>(new MyResponse().withMessage("The patient does not exists"),
                    HttpStatus.BAD_REQUEST);
        }

        Appointment appointment = new Appointment(req, patients.get(0), doctors.get(0));

        appointmentRepository.save(appointment);
        return new ResponseEntity<>(new MyResponse().withMessage("OK"), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/appointments/fetch")
    public ResponseEntity<MyResponse> getAppoitment(@RequestParam String name) {

        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<>(new MyResponse().withMessage("Name is required"), HttpStatus.BAD_REQUEST);
        }

        List<User> users = userRepository.findAllByName(name);

        if (users != null && users.isEmpty()) {
            return new ResponseEntity<>(new MyResponse().withMessage("The user does not exists"),
                    HttpStatus.BAD_REQUEST);
        }


        User user = new User();
        user.setName(name);

        if (users.get(0).getRole().equals("doctor")) {
            return new ResponseEntity<>(new MyResponse().withAppointments(appointmentRepository.findAllByDoctor(user)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MyResponse().withAppointments(appointmentRepository.findAllByPatient(user)),
                    HttpStatus.OK);
        }
    }
}
