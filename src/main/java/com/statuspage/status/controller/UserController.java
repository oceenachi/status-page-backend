package com.statuspage.status.controller;

import com.statuspage.status.Repository.UserRepository;
import com.statuspage.status.dto.request.SigninPayload;
import com.statuspage.status.dto.response.CreateResponse;
import com.statuspage.status.model.User;
import com.statuspage.status.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private UserService userService;

    @Autowired
    private UserController(UserRepository userRepository, UserService userService, ModelMapper modelMapper){
        this.userRepository=userRepository;
        this.modelMapper=modelMapper;
        this.userService=userService;
    }


    @PostMapping("/register")
    private ResponseEntity<CreateResponse> register(@Valid @RequestBody SigninPayload signinPayload){
        User user = modelMapper.map(signinPayload, User.class);
        CreateResponse createResponse = userService.register(user);
        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }


}
