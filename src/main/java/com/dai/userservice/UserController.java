package com.dai.userservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HashingService hashingService;

    private static final Logger log = LogManager.getLogger(UserController.class);

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/users/register", consumes = "application/json")
    public ResponseEntity<MyResponse> saveUser(@RequestBody User user, HttpServletRequest req
                                               ) {

        log.info("Request to create user " + user.getUserName());

        if (!checkUserFields(user)) {
            return new ResponseEntity<>(new MyResponse().withMessage("Invalid user fields"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findById(user.getUserName()).isPresent()) {
            return new ResponseEntity<>(new MyResponse().withMessage("The user already exists"), HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setName(user.getName());
        newUser.setRole(user.getRole());
        try {
            newUser.setPassword(hashingService.digest(user.getPassword()));
        } catch (Exception e) {
            log.error("Someting went wrong while hashing " + e);
            return new ResponseEntity<>(new MyResponse().withMessage("Failed hashing the password"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userRepository.save(newUser);
        req.getSession().setAttribute("user", newUser);
        log.info("Created user " + newUser.getUserName() + " with hashed pass " + newUser.getPassword());

        return new ResponseEntity<>(new MyResponse().withMessage("OK"), HttpStatus.OK);
    }

    @PostMapping(value = "/users/validate")
    @CrossOrigin(origins = "*")
    public ResponseEntity<MyResponse> findUser(@RequestBody User user, HttpServletRequest req, HttpServletResponse response) {

        log.info("Request to validate user " + user.getUserName());

        req.getSession().getAttribute("user");

        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            return new ResponseEntity<>(new MyResponse().withMessage("Invalid user fields"), HttpStatus.BAD_REQUEST);
        }

        Optional<User> savedUser = userRepository.findById(user.getUserName());

        String hashedPass;
        try {
            hashedPass = hashingService.digest(user.getPassword());
        } catch (Exception e) {
            log.error("Someting went wrong while hashing " + e);
            return new ResponseEntity<>(new MyResponse().withMessage("Failed hashing the password"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (savedUser.isPresent() && savedUser.get().getPassword().equals(hashedPass)) {
            return new ResponseEntity<>(new MyResponse().withMessage("OK").withUser(savedUser.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MyResponse().withMessage("The password and username do not match"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/users/names")
    public ResponseEntity<MyResponse> getNames( @RequestParam String role) {

        if (StringUtils.isEmpty(role)) {
            return new ResponseEntity<>(new MyResponse().withMessage("Role is required"), HttpStatus.BAD_REQUEST);
        }

        if (!role.equals("doctor") && !role.equals("patient")) {
            return new ResponseEntity<>(new MyResponse().withMessage("Role must either doctor or patient"), HttpStatus.BAD_REQUEST);
        }

        List<User> users = userRepository.findAll();

        List<String> names = users.stream()
                .filter(x -> x.getRole().equals(role))
                .map(User::getName)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new MyResponse().withNames(names), HttpStatus.OK);
    }


    private boolean checkUserFields(User user) {
        return !StringUtils.isEmpty(user.getPassword()) && !StringUtils.isEmpty(user.getUserName())
                && !StringUtils.isEmpty(user.getName()) && user.getRole() != null;
    }
}
