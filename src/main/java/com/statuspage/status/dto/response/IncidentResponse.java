package com.statuspage.status.dto.response;
import com.statuspage.status.model.IncidentStatus;
import lombok.Data;

@Data
public class IncidentResponse {

    private String fullDateTime;

    private IncidentStatus incidentStatus;

    private String message;
}
