package com.statuspage.status.service;

import com.statuspage.status.Repository.IncidentRepository;
import com.statuspage.status.Repository.RequestRepository;
import com.statuspage.status.Repository.UserRepository;
import com.statuspage.status.Repository.WebsiteRepository;
import com.statuspage.status.dto.response.CreateResponse;
import com.statuspage.status.exception.WebsiteAlreadyExistsException;
import com.statuspage.status.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Service
public class AllServices {

    private static final Logger logger = LoggerFactory.getLogger(AllServices.class);
    private final WebsiteRepository websiteRepository;
    private final RequestRepository requestRepository;
    private final IncidentRepository incidentRepository;
    private final EmailServiceImpl emailService;
    private final UserRepository userRepository;

    @Autowired
    private AllServices(WebsiteRepository websiteRepository, IncidentRepository incidentRepository,
                        RequestRepository requestRepository,
                        EmailServiceImpl emailService, UserRepository userRepository) {
        this.websiteRepository = websiteRepository;
        this.requestRepository = requestRepository;
        this.incidentRepository = incidentRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public CreateResponse createWebsite(Website newWebsite) {
        try {
            Optional<Website> website = websiteRepository.findByUrl(newWebsite.getUrl());
            if (!website.isPresent()) {
                websiteRepository.save(newWebsite);
                return new CreateResponse(newWebsite.getName(), "Website created successfully");
            } else {
                throw new WebsiteAlreadyExistsException("The website ' " + newWebsite.getName() + "' already exists");
            }
        } catch (Exception ex) {
            throw new WebsiteAlreadyExistsException("the website ' " + newWebsite.getName() + "' already exists");
        }
    }

    public List<Website> getAllWebsites() {
        return websiteRepository.findAll();
    }
}