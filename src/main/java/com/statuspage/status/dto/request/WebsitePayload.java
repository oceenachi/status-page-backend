package com.statuspage.status.dto.request;

import com.statuspage.status.model.StatusName;
import lombok.Data;


@Data
public class WebsitePayload {

    private String name;

    private String url;

    private String description;

    private StatusName currentStatus;
}
