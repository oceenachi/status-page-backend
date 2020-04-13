package com.statuspage.status.service;

import com.statuspage.status.Repository.WebsiteRepository;
import com.statuspage.status.model.Website;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class AsyncServices {

    WebsiteRepository websiteRepository;
    RestTemplate restTemplate;

    public AsyncServices(WebsiteRepository websiteRepository, RestTemplate restTemplate){
        this.websiteRepository = websiteRepository;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> getResponses(String url){
        return restTemplate.getForEntity(url, String.class);
    }


    @PostConstruct
    private void makeAsyncCalls() throws ExecutionException, InterruptedException {
        List<Website> websites = websiteRepository.findAll();

        List<String> urls = websites.stream().map(Website::getUrl).collect(Collectors.toList());
        System.out.println(urls);

        for(String url: urls){
            Future<Integer> response = CompletableFuture.supplyAsync(() -> this.getResponses(url).getStatusCodeValue());

            Integer statusCode = response.get();
            System.out.println(statusCode);
        }

    }






}
