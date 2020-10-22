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
public interface IncidentRepository extends JpaRepository<Incident, UUID>, JpaSpecificationExecutor<Incident> {

//    @Query("select incident from Incident incident order by incident.groupNumber desc")
//    Page<Incident> findLastGroupNumber(PageRequest pageable);
//
//    @Query("select incident from Incident incident where incident.incidentTime >= ?2 AND incident.incidentTime  <= ?1 order by incident.groupNumber desc")
//    List<Incident> getRecentTwelve(Instant now, Instant twelfthDay);

    @Query(value = "select * from incident where incident_time between :twelveDaysAgo and :latest", nativeQuery = true)
    List<Incident> getRecentIncidents(@Param("twelveDaysAgo") Long twelveDaysAgo, @Param("latest") Long latest);
}
