package com.statuspage.status.Repository;

import com.statuspage.status.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;


@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID>{


    @Query(value = "SELECT message, incident_status, is_resolved, name, incident_time, " +
            "FLOOR(incident_time/86400000) as incident_group from incident inner join request on " +
            "incident.request_id = request.request_id inner join website on website.url = request.website_url " +
            "where incident_time BETWEEN :twelveDaysAgo AND :latest GROUP BY name, incident_group order by incident_group",
            nativeQuery = true)
    List<IncidentMetrics> getRecentIncidents(@Param("twelveDaysAgo") Long twelveDaysAgo, @Param("latest") Long latest);
}
