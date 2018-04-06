package com.dai.userservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HashingService hashingService;

    private static final Logger log = LogManager.getLogger(UserController.class);

    @PostMapping(value = "/users/register", consumes = "application/json")
    public ResponseEntity<MyResponse> saveUser(@RequestBody User user) {

        log.info("Request to create user " + user.getUserName());

        if (!checkUserFields(user)) {
            return new ResponseEntity<>(new MyResponse("Invalid user fields"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findById(user.getUserName()).isPresent()) {
            return new ResponseEntity<>(new MyResponse("The user already exists"), HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setUserName(user.getUserName());
        try {
            newUser.setPassword(hashingService.digest(user.getPassword()));
        } catch (Exception e) {
            log.error("Someting went wrong while hashing " + e);
            return new ResponseEntity<>(new MyResponse("Failed hashing the password"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userRepository.save(newUser);
        log.info("Created user " + newUser.getUserName() + " with hashed pass " + newUser.getPassword());

        return new ResponseEntity<>(new MyResponse("OK"), HttpStatus.OK);
    }

    @PostMapping(value = "/users/validate")
    public ResponseEntity<MyResponse> findUser(@RequestBody User user) {

        log.info("Request to validate user " + user.getUserName());

        if (!checkUserFields(user)) {
            return new ResponseEntity<>(new MyResponse("Invalid user fields"), HttpStatus.BAD_REQUEST);
        }

        Optional<User> savedUser = userRepository.findById(user.getUserName());

        String hashedPass;
        try {
            hashedPass = hashingService.digest(user.getPassword());
        } catch (Exception e) {
            log.error("Someting went wrong while hashing " + e);
            return new ResponseEntity<>(new MyResponse("Failed hashing the password"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (savedUser.isPresent() && savedUser.get().getPassword().equals(hashedPass)) {
            return new ResponseEntity<>(new MyResponse("OK"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MyResponse("The password and username do not match"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkUserFields(User user) {
        return !StringUtils.isEmpty(user.getPassword()) && !StringUtils.isEmpty(user.getUserName());
    }
}
