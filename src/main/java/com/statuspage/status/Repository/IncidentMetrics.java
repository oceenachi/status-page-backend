package com.statuspage.status.Repository;

import com.statuspage.status.model.IncidentStatus;

public interface IncidentMetrics {

    String getMessage();
    boolean getIs_resolved();
    String getName();
    IncidentStatus getIncident_status();
    Long getIncident_time();
    Long getIncident_group();
}
