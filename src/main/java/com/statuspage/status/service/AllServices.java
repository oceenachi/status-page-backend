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

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
public class AllServices {

    private WebsiteRepository websiteRepository;
    private RestTemplate restTemplate;
    private RequestRepository requestRepository;
    private IncidentRepository incidentRepository;
    private EmailServiceImpl emailService;
    private UserRepository userRepository;

    @Autowired
    private AllServices(WebsiteRepository websiteRepository, IncidentRepository incidentRepository,
                        RestTemplate restTemplate, RequestRepository requestRepository, EmailServiceImpl emailService, UserRepository userRepository) {
        this.websiteRepository = websiteRepository;
        this.restTemplate = restTemplate;
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
                return new CreateResponse(newWebsite.getName(), "Website created created successfully");
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



    @Scheduled(cron = "*/5 * * * * ?")
    public void fireRequest() {

        Request newRequest = new Request();
        Incident newIncident = new Incident();
        boolean sent = false;
//        try {
            List<Website> websiteList = websiteRepository.findAll();
            for (Website website : websiteList) {
                String url = website.getUrl();
                ResponseEntity<?> responseEntity = restTemplate.getForEntity(url, String.class);
                int value = responseEntity.getStatusCodeValue();
                newRequest.setWebsite(website);
                newRequest.setResponseCode(value);
                newRequest.setRequestTime(Instant.now());
                System.out.println(website.getName());

                requestRepository.save(newRequest);

                if (value < 300) {
                    sent = false;
                    website.setCurrentStatus(StatusName.Operational);

                    if (!newIncident.getIsResolved()) {
                        newIncident.setIsResolved(true);
                    }

                    if (newIncident.getIncidentStatus() != IncidentStatus.Resolved) {
                        newIncident.setIncidentStatus(IncidentStatus.Resolved);
                    }

                } else {
                    System.out.println("currently down, currently down");
                    website.setCurrentStatus(StatusName.Unserviceable);

                    newIncident.setIsResolved(false);
                    newIncident.setIncidentTime(Instant.now());
                    String newMessage = website.getName() + " is currently down for some users" + '\n' + "Our engineering team is currently working hard to resolve this issue";
                    newIncident.setMessage(newMessage);
                    newIncident.setIncidentStatus(IncidentStatus.Investigating);
                    newIncident.setRequest(newRequest);
                    incidentRepository.save(newIncident);

//                    if(newIncident.getIsResolved() && (sent)){
//                        System.out.println("problem here");
//                        String body = "As at " + newIncident.getIncidentTime() + ", "+ website.getName() + " returned a status code of "
//                                + responseEntity.getStatusCode() + ". " + "\n" + "Here is a redirection link to server http://localhost:5000";
//                        List<String> allUserEmails = userRepository.findAllByEmail();
//
//
//                        String[] emails = new String[allUserEmails.size()];
//                        int count = 0;
//                        for(String email: allUserEmails){
//                            emails[count] = email;
//                            count++;
//                        }
//                        emailService.sendSimpleMessage(emails, website.getName() + " crash", body);
//                        sent = true;
//
//                    }

                }
            }

//        } catch (Exception ex) {
//            throw new RequestFailException("Requests could not be executed");
//
//        }

    }
}