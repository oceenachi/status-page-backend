package com.statuspage.status.dto.response;
import com.statuspage.status.model.Incident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentResponse {

    private HashMap<Long, List<Incident>> payload;
}
