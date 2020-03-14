package com.statuspage.status.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "incident")
public class Incident {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID incidentId=UUID.randomUUID();

    private IncidentStatus incidentStatus;

    private String message;

    private Boolean isResolved=false;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    private Instant incidentTime;
}
