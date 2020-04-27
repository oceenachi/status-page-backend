package com.statuspage.status.dto.request;

import com.statuspage.status.model.Incident;
import lombok.Data;


@Data
public class IncidentUrl {

    private Incident incident;

    private String incidentUrl;
}
