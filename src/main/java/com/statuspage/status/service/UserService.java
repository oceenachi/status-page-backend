package com.statuspage.status.service;


import com.statuspage.status.Repository.UserRepository;
import com.statuspage.status.dto.response.CreateResponse;
import com.statuspage.status.exception.UserAlreadyExistsException;
import com.statuspage.status.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public CreateResponse register(User user){
        try{
            Optional<User> newUser= userRepository.findByEmail(user.getEmail());

            if(!newUser.isPresent()){
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                logger.info("A new user has been created", user);
                CreateResponse createResponse = new CreateResponse(user.getName(), "new user created successfully");
                return createResponse;
            }
            else {
                throw new UserAlreadyExistsException("User with email '" + user.getEmail() + "' already exists");
            }
        }
        catch (Exception ex){
            throw new UserAlreadyExistsException("User with email '" + user.getEmail() + "'already exists");
        }
    }

}
