package com.dai.userservice.results;

import com.dai.userservice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ResultController {

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    UserRepository userRepository;

    private static final Logger log = LogManager.getLogger(ResultController.class);

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/results/create", consumes = "application/json")
    public ResponseEntity<MyResponse> addResult(@RequestBody AddResultReq req) {

        log.info("Request to add result " + req.getDescription() + "-" + req.getName());

        if (!checkResultsFields(req)) {
            return new ResponseEntity<>(new MyResponse().withMessage("Bad input data"), HttpStatus.BAD_REQUEST);
        }

        List<User> users = userRepository.findAllByName(req.getName());
        if (users.isEmpty()) {
            return new ResponseEntity<>(new MyResponse().withMessage("User does not exists"), HttpStatus.BAD_REQUEST);
        }

        Result result = new Result();
        result.setDescription(req.getDescription());
        result.setTitle(req.getTitle());
        result.setUser(users.get(0));

        resultRepository.save(result);
        log.info("Adding result succedeed");
        return new ResponseEntity<>(new MyResponse().withMessage("OK"), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/results/fetch", consumes = "application/json")
    public ResponseEntity<MyResponse> getResult(@RequestParam(required = true) String name) {
        log.info("Request to get results for " + name);

        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<>(new MyResponse().withMessage("Name is required"), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(name);
        return  new ResponseEntity<>(new MyResponse().withResults(resultRepository.findAllByUser(user)),
                HttpStatus.OK);
    }

    private boolean checkResultsFields(AddResultReq req) {
        return !StringUtils.isEmpty(req.getName()) && !StringUtils.isEmpty(req.getTitle())
                && !StringUtils.isEmpty(req.getDescription());
    }
}
