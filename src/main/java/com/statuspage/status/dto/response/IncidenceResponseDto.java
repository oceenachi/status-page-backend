package com.statuspage.status.dto.response;


import lombok.Data;

import java.time.Instant;

@Data
public class IncidenceResponseDto {

    private String websiteName;

    private String message;

    private Instant time;
}
