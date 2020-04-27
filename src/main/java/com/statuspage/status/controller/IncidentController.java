package com.statuspage.status.controller;


import com.statuspage.status.model.Incident;
import com.statuspage.status.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/incident")
public class IncidentController {

    private IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService){
        this.incidentService = incidentService;
    }

    @GetMapping
    public List<Incident> getIncidents(){
        return incidentService.getIncidents();

    }

    @GetMapping("/past")
    public ResponseEntity<?> getPastIncident(){
        return incidentService.pastIncidents();
    }

    @GetMapping("/batched")
    public ResponseEntity<?> getIncidentForMonth(){
        return incidentService.getBatched();
    }



}
