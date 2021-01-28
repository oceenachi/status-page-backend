package com.statuspage.status.model;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Data
@Entity
@Table(name = "website", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        }),
        @UniqueConstraint(columnNames = {
                "url"
        })
})
public class Website {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "uuid2"
    )
    @Column(columnDefinition = "BINARY(16)")
    private UUID websiteId;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 60)
    private String url;

    @NotBlank
    @Size(max = 100)
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusName currentStatus;

    @CreationTimestamp
    private Timestamp timestamp;

}
