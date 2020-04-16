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
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
public class AllServices {

    private WebsiteRepository websiteRepository;
//    private RestTemplate restTemplate;
    private RequestRepository requestRepository;
    private IncidentRepository incidentRepository;
    private EmailServiceImpl emailService;
    private UserRepository userRepository;

    @Autowired
    private AllServices(WebsiteRepository websiteRepository, IncidentRepository incidentRepository,
                        RequestRepository requestRepository,
                        EmailServiceImpl emailService, UserRepository userRepository) {
        this.websiteRepository = websiteRepository;
//        this.restTemplate = restTemplate;
        this.requestRepository = requestRepository;
        this.incidentRepository = incidentRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(AllServices.class);


    public CreateResponse createWebsite(Website newWebsite) {
        try {
            System.out.println(newWebsite);
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


//    public void fireRequest() {


////
//////                    if(newIncident.getIsResolved() && (sent)){
//////                        System.out.println("problem here");
//////                        String body = "As at " + newIncident.getIncidentTime() + ", "+ website.getName() + " returned a status code of "
//////                                + responseEntity.getStatusCode() + ". " + "\n" + "Here is a redirection link to server http://localhost:5000";
//////                        List<String> allUserEmails = userRepository.findAllByEmail();
//////
//////
//////                        String[] emails = new String[allUserEmails.size()];
//////                        int count = 0;
//////                        for(String email: allUserEmails){
//////                            emails[count] = email;
//////                            count++;
//////                        }
//////                        emailService.sendSimpleMessage(emails, website.getName() + " crash", body);
//////                        sent = true;
//////
//////                    }
////
////                }
////            }
////
////        } catch (Exception ex) {
//////            throw new RequestFailException("Requests could not be executed");
////            System.out.println(Arrays.toString(ex.getStackTrace()));
////
//        }
//
//    }
}