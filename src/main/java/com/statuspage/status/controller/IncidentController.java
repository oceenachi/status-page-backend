package com.statuspage.status.controller;


import com.statuspage.status.model.Incident;
import com.statuspage.status.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/incident")
public class IncidentController {

    private IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService){
        this.incidentService = incidentService;
    }

    @GetMapping("/allIncidents")
    public List<Incident> getIncidents(){
        return incidentService.getIncidents();

    }

//    @GetMapping("")
//    public Incident getIncident(@PathVariable("incidentID") ){
//
//    }


}
