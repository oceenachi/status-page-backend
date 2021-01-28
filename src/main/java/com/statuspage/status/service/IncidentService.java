package com.statuspage.status.service;

import com.statuspage.status.Repository.IncidentMetrics;
import com.statuspage.status.Repository.IncidentRepository;
import com.statuspage.status.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class IncidentService {

    IncidentRepository incidentRepository;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository){
        this.incidentRepository = incidentRepository;
    }

    public List<Incident> getIncidents(){
        return incidentRepository.findAll();
    }

    public List<IncidentMetrics> getPastIncidents(){

        Calendar date = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        Long twelveDaysAgo = date.toInstant().minus(12, ChronoUnit.DAYS).toEpochMilli();

        List<IncidentMetrics> recentIncidents = incidentRepository.getRecentIncidents(twelveDaysAgo, Instant.now().toEpochMilli());
        System.out.println("recentIncidents" + recentIncidents);

        return recentIncidents;
    }

//    public ResponseEntity<List<IncidentMetrics>> pastIncidents(){
//


}
