package com.statuspage.status.service;

import com.statuspage.status.Repository.IncidentRepository;
import com.statuspage.status.Repository.RequestRepository;
import com.statuspage.status.Repository.WebsiteRepository;
import com.statuspage.status.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class AsyncServices {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    WebsiteRepository websiteRepository;
    RequestRepository requestRepository;
    IncidentRepository incidentRepository;
    private RestTemplate restTemplate;

    @Autowired
    public AsyncServices(WebsiteRepository websiteRepository, RestTemplate restTemplate,
                         RequestRepository requestRepository, IncidentRepository incidentRepository) {
        this.websiteRepository = websiteRepository;
        this.restTemplate = restTemplate;
        this.requestRepository = requestRepository;
        this.incidentRepository = incidentRepository;
    }


    public ResponseEntity<?> getResponses(String url) {
        return restTemplate.getForEntity(url, String.class);
    }

    public int processResponse(String url, int responseCode){
        Request newRequest = new Request();
        newRequest.setResponseCode(responseCode);
        newRequest.setRequestTime(Instant.now());
        newRequest.setWebsiteUrl(url);
        requestRepository.save(newRequest);

        if(responseCode >= 300){
            responseFailure(newRequest, url);
        }else{
            responseSuccess(url);
        }
        return responseCode;

    }

    public void responseSuccess( String url){
        websiteRepository.updateWebsiteStatus(StatusName.Operational, url);

    }

    public void responseFailure(Request newRequest, String url){
        websiteRepository.updateWebsiteStatus(StatusName.Unserviceable, url);

        Incident newIncident = new Incident();
        newIncident.setIncidentStatus(IncidentStatus.Investigating);

        Optional<Website> downWebsite = websiteRepository.findByUrl(url);
        assert downWebsite.isPresent();

        String newMessage = downWebsite.get().getName().toLowerCase() + " is currently down for some users. " + '\n' +
                "Our engineering team is currently investigating this issue";

        Instant baseDate = Instant.parse("2020-04-16T05:29:06.196735200Z");
        Long duration = Duration.between(baseDate, Instant.now()).toHours();

        newIncident.setMessage(newMessage);
        newIncident.setIncidentTime(Instant.now());
        newIncident.setIsResolved(false);
        newIncident.setRequest(newRequest);
        newIncident.setGroupNumber(duration / 24);
        incidentRepository.save(newIncident);

    }

    @Scheduled(cron = "* */5 * * * ?")
    private void makeAsyncCalls() throws ExecutionException, InterruptedException, TimeoutException {
        List<Website> websites = websiteRepository.findAll();

        List<String> urls = websites.stream().map(Website::getUrl).collect(Collectors.toList());

        for (String url : urls) {

                CompletableFuture<Integer> response = CompletableFuture.supplyAsync(() -> {
                    try{
                        return this.getResponses(url).getStatusCodeValue();
                    }catch (Exception ex){
                        throw new IllegalArgumentException(ex.getMessage());
                    }
                }).thenApplyAsync(res-> processResponse(url, res))

                        .exceptionally(exception -> {
                            log.info(String.valueOf(exception));
                            return HttpStatus.SERVICE_UNAVAILABLE.value();
                        }).thenApplyAsync(res-> processResponse(url, res));
            log.info("url {} {}", url, response.get(10000, TimeUnit.SECONDS));
        }

    }

}
