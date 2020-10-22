package com.statuspage.status.service;

import com.statuspage.status.Repository.IncidentRepository;
import com.statuspage.status.dto.response.IncidenceResponseDto;
import com.statuspage.status.dto.response.IncidentResponse;
import com.statuspage.status.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

//    /**
//     * Get a diff between two dates
//     * @param date1 the oldest date
//     * @param date2 the newest date
//     * @param timeUnit the unit in which you want the diff
//     * @return the diff value, in the provided unit
//     */
    @PostConstruct
    private Map<Long, List<Incident>> getRecentPastIncidents(){

//        Instant now = Instant.now();
//        Instant twelfthDay = now.minus(12, ChronoUnit.DAYS);
//
//        List<Incident> recentIncidents = incidentRepository
//                .getRecentTwelve(now, twelfthDay);
//        System.out.println("recent incidents: " + recentIncidents);
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
//        String strDate = formatter.format(date);

//        System.out.println("strDate ts " + Timestamp.valueOf(strDate));
//        System.out.println("newDt " + new Date());
//        System.out.println("millis " +(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5)).toString()));
//        System.out.println("ts " + Timestamp.valueOf((new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5)).toString())));
        Long twelveDaysAgo = Instant.now().minus(12, ChronoUnit.DAYS).toEpochMilli();
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(twelveDaysAgo);
        List<Incident> recentIncidents = incidentRepository.getRecentIncidents(twelveDaysAgo, Instant.now().toEpochMilli());
        System.out.println("recentIncidents" + recentIncidents);
        Hashtable<Long, List<Incident>> groupNumberAndIncidence = new Hashtable<>();



//        for(Incident oneIncident: recentIncidents){
//
//            if(groupNumberAndIncidence.containsKey(oneIncident.getGroupNumber())){
//                groupNumberAndIncidence.get(oneIncident.getGroupNumber()).add(oneIncident);
//            }else{
//                groupNumberAndIncidence.put(oneIncident.getGroupNumber(), new ArrayList<Incident>(Arrays.asList(oneIncident)));
//            }
//        }

        return groupNumberAndIncidence;
    }


    private HashMap<Long, List<IncidenceResponseDto>> formatIncidenceResponse(Map<Long, List<Incident>> incidentResults){
        IncidentResponse incidentResponse = new IncidentResponse();
        HashMap<Long, List<IncidenceResponseDto>> responseMap = new HashMap<>(15);
        for(Map.Entry<Long, List<Incident>> entrySet: incidentResults.entrySet()){
            List<IncidenceResponseDto> incidentList = new ArrayList<IncidenceResponseDto>(10);
            Set<String> uniqueUrls = new HashSet<>();
            for(Incident incident: entrySet.getValue()){
                if(!uniqueUrls.contains(incident.getRequest().getWebsiteUrl())){
                    uniqueUrls.add(incident.getRequest().getWebsiteUrl());
                    IncidenceResponseDto incidenceResponseDto = new IncidenceResponseDto();
                    incidenceResponseDto.setMessage(incident.getMessage());
                    incidenceResponseDto.setIncidentTime(incident.getIncidentTime());
                    incidenceResponseDto.setWebsiteName(incident.getRequest().getWebsiteUrl());
                    incidenceResponseDto.setIncidentStatus(incident.getIncidentStatus());
                    incidentList.add(incidenceResponseDto);
                }System.out.println(uniqueUrls);
            }
          responseMap.put(entrySet.getKey(), incidentList);

            }
        return responseMap;

    }

    public ResponseEntity<HashMap<Long, List<IncidenceResponseDto>>> pastIncidents(){

        Map<Long, List<Incident>> recentPastIncident = this.getRecentPastIncidents();
        HashMap<Long, List<IncidenceResponseDto>> uniqueIncidences = formatIncidenceResponse(recentPastIncident);
        return new ResponseEntity<HashMap<Long, List<IncidenceResponseDto>>>(uniqueIncidences, HttpStatus.ACCEPTED);
    }





}
