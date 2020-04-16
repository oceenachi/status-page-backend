package com.statuspage.status.model;

import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;


@Data
@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "uuid2"
    )
    @Column(columnDefinition = "BINARY(16)")
    private UUID requestId;

    private String websiteUrl;

    @NotNull
    private int responseCode;

    @CreationTimestamp
    private Instant requestTime;
}
