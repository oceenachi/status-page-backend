package com.statuspage.status.dto.response;
import com.statuspage.status.model.IncidentStatus;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class IncidenceResponseDto {

    private String websiteName;

    private String message;

    private Long incidentTime;

    private IncidentStatus incidentStatus;

    private Boolean isResolved;
}
