package com.statuspage.status.service;

import com.statuspage.status.Repository.IncidentRepository;
import com.statuspage.status.dto.response.IncidenceResponseDto;
import com.statuspage.status.dto.response.IncidentResponse;
import com.statuspage.status.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IncidentService {

    private IncidentRepository incidentRepository;

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
//    @PostConstruct
    private Map<Long, List<Incident>> getRecentPastIncidents(){
        Long lastGroupNumber = Long.MIN_VALUE;
        Page<Incident> lastGroupIncident = incidentRepository.findLastGroupNumber(PageRequest.of(0,1));
        for(Incident lastIncident: lastGroupIncident){
            lastGroupNumber = lastIncident.getGroupNumber();
        }

        List<Incident> recentIncidents = incidentRepository
                .getRecentTwelve(lastGroupNumber);
        System.out.println("recent incidents: " + recentIncidents);

        Hashtable<Long, List<Incident>> groupNumberAndIncidence = new Hashtable<>();
        for(Incident oneIncident: recentIncidents){
            if(groupNumberAndIncidence.containsKey(oneIncident.getGroupNumber())){
                groupNumberAndIncidence.get(oneIncident.getGroupNumber()).add(oneIncident);
            }else{
                groupNumberAndIncidence.put(oneIncident.getGroupNumber(), new ArrayList<Incident>(Arrays.asList(oneIncident)));
            }
        }
        System.out.println(groupNumberAndIncidence);


        return groupNumberAndIncidence;
    }


    private List<HashMap<Long, List<IncidenceResponseDto>>> formatIncidenceResponse(Map<Long, List<Incident>> incidentResults){
        IncidentResponse incidentResponse = new IncidentResponse();
        List<HashMap<Long, List<IncidenceResponseDto>>> responseList = new ArrayList<>(15);
        for(Map.Entry<Long, List<Incident>> entrySet: incidentResults.entrySet()){
            HashMap<Long, List<IncidenceResponseDto>> newResponse = new HashMap<>();
            List<IncidenceResponseDto> incidentList = new ArrayList<IncidenceResponseDto>(10);
            Set<String> uniqueUrls = new HashSet<>();
            for(Incident incident: entrySet.getValue()){
                if(!uniqueUrls.contains(incident.getRequest().getWebsiteUrl())){
                    uniqueUrls.add(incident.getRequest().getWebsiteUrl());
                    IncidenceResponseDto incidenceResponseDto = new IncidenceResponseDto();
                    incidenceResponseDto.setMessage(incident.getMessage());
                    incidenceResponseDto.setTime(incident.getIncidentTime());
                    incidenceResponseDto.setWebsiteName(incident.getRequest().getWebsiteUrl());
                    incidenceResponseDto.setIncidentStatus(incident.getIncidentStatus());
                    incidentList.add(incidenceResponseDto);
                }System.out.println(uniqueUrls);
            }
            newResponse.put(entrySet.getKey(), incidentList);
            responseList.add(newResponse);

        }
        return responseList;
    }

    public ResponseEntity<List<HashMap<Long, List<IncidenceResponseDto>>>> pastIncidents(){

        Map<Long, List<Incident>> recentPastIncident = this.getRecentPastIncidents();
        List<HashMap<Long, List<IncidenceResponseDto>>> uniqueIncidences = formatIncidenceResponse(recentPastIncident);
        return new ResponseEntity<List<HashMap<Long, List<IncidenceResponseDto>>>>(uniqueIncidences, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> getBatched(){
//        List<Incident> batchdIncident = incidentRepository.
        return null;
    }
}
