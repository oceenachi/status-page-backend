package com.statuspage.status.service;

import com.statuspage.status.Repository.WebsiteRepository;
import com.statuspage.status.model.Request;
import com.statuspage.status.model.Website;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class AsyncServices {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    WebsiteRepository websiteRepository;

    @Autowired
    private RestTemplate restTemplate;

    public AsyncServices(WebsiteRepository websiteRepository, RestTemplate restTemplate) {
        this.websiteRepository = websiteRepository;
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<?> getResponses(String url) {
        return restTemplate.getForEntity(url, String.class);
    }


    @PostConstruct
    private void makeAsyncCalls() throws ExecutionException, InterruptedException, TimeoutException {
        List<Website> websites = websiteRepository.findAll();
//        list of urls
//        [https://www.whogohost.ng/, https://www.nairaland.com/, https://stackoverflow.com/,
//        https://bitly.com/, https://www.carmax.com/, https://www.betexplorer.com/soccer/australia/]
        List<String> stringListOfWebsites = new ArrayList<>();
        stringListOfWebsites.add("https://www.whogohost.ng/");
        stringListOfWebsites.add("https://www.nairaland.com");
        stringListOfWebsites.add("https://stackoverflow.com/");
        stringListOfWebsites.add("https://bitly.com");
        stringListOfWebsites.add("https://www.carmax.com/");
        stringListOfWebsites.add("https://www.betexplorer.com/soccer/australia/");


        List<String> urls = websites.stream().map(Website::getUrl).collect(Collectors.toList());
        System.out.println(urls);

        for (String url : urls) {
            Future<Integer> response = CompletableFuture.supplyAsync(() -> this.getResponses(url).getStatusCodeValue());

            System.out.println(url + response.get(10000, TimeUnit.SECONDS));
        }


        for (String url : stringListOfWebsites) {

            CompletableFuture<Integer> response = CompletableFuture.supplyAsync(() -> {
                try {

                    return getResponses(url).getStatusCodeValue();

                } catch (Exception ex) {
                    //throw your custom exception here
                    throw new IllegalArgumentException(ex.getMessage());
                }

            }).thenApplyAsync(req -> req) //This {thenApplyAsync} block is in case you want to do another thing with the
                    // response from the try block above, you can take it out if there's nothing to be done

                    //you can return whatever error you want here, i just decided to return 1
                    //This block is the reason your app was crashing you were not handling it.
                    .exceptionally(exception -> {
                        log.info(String.valueOf(exception));
                        return 1;
                    });

            log.info("url {} {}", url, response.get(10000, TimeUnit.SECONDS));
        }

    }

}
