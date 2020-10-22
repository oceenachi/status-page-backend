package com.statuspage.status.model;


import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "incident")
public class Incident {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "uuid2"
    )
    @Column(columnDefinition = "BINARY(16)")
    private UUID incidentId;

    private IncidentStatus incidentStatus;

    @NotBlank
    @Size(max = 2052)
    private String message;

    private Boolean isResolved=false;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @NotNull
    private Long incidentTime;

}
