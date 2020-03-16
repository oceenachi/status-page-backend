package com.statuspage.status.controller;

import com.statuspage.status.dto.request.WebsitePayload;
import com.statuspage.status.dto.response.CreateResponse;
import com.statuspage.status.model.Website;
import com.statuspage.status.service.AllServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class WebsiteController {

    private ModelMapper modelMapper;
    private AllServices allServices;

    @Autowired
    public WebsiteController(ModelMapper modelMapper, AllServices allServices){
        this.modelMapper = modelMapper;
        this.allServices = allServices;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateResponse> createWebsite(@RequestBody WebsitePayload websiteInfo){
        Website website = modelMapper.map(websiteInfo, Website.class);
        CreateResponse createResponse = allServices.createWebsite(website);
        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    @GetMapping("/websites")
    public List<Website> getAllWebsites(){
        return allServices.getAllWebsites();
    }


}
