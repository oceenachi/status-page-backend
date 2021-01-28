package com.statuspage.status.service;

import com.statuspage.status.Repository.IncidentRepository;
import com.statuspage.status.Repository.RequestRepository;
import com.statuspage.status.Repository.WebsiteRepository;
import com.statuspage.status.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
                         RequestRepository requestRepository, IncidentRepository incidentRepository,
                         MessageQueueProducer messageQueueProducer) {
        this.websiteRepository = websiteRepository;
        this.restTemplate = restTemplate;
        this.requestRepository = requestRepository;
        this.incidentRepository = incidentRepository;
    }


    public ResponseEntity<?> getResponses(String url) throws Exception {
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

            return result;
    }

    public int processResponse(String url, int responseCode){
        Request newRequest = new Request();
        newRequest.setResponseCode(responseCode);
        newRequest.setRequestTime(Instant.now().toEpochMilli());
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
        downWebsite.ifPresent(website -> {

        });

        String newMessage = downWebsite.get().getName().toLowerCase() + " is currently down for some users. " + '\n' +
                "Our engineering team is currently investigating this issue";

        newIncident.setMessage(newMessage);
        newIncident.setIncidentTime(Instant.now().toEpochMilli());
        newIncident.setIsResolved(false);
        newIncident.setRequest(newRequest);
        incidentRepository.save(newIncident);       

    }

    @Scheduled(cron = "* */1 * * * ?")
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
