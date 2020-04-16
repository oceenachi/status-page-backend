package com.statuspage.status.service;

import com.statuspage.status.Repository.WebsiteRepository;
import com.statuspage.status.model.Request;
import com.statuspage.status.model.Website;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;
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
    private void makeAsyncCalls() throws ExecutionException, InterruptedException, TimeoutException {
        List<Website> websites = websiteRepository.findAll();
//        list of urls
//        [https://www.whogohost.ng/, https://www.nairaland.com/, https://stackoverflow.com/,
//        https://bitly.com/, https://www.carmax.com/, https://www.betexplorer.com/soccer/australia/]


        List<String> urls = websites.stream().map(Website::getUrl).collect(Collectors.toList());
        System.out.println(urls);

        for(String url: urls){
            Future<Integer> response = CompletableFuture.supplyAsync(() -> this.getResponses(url).getStatusCodeValue());

            System.out.println(url + response.get(10000, TimeUnit.SECONDS));
        }

    }


}
