package com.statuspage.status.Repository;

import com.statuspage.status.model.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    @Query("select incident from Incident incident order by incident.groupNumber desc")
    Page<Incident> findLastGroupNumber(PageRequest pageable);

    @Query("select incident from Incident incident where ?1 - incident.groupNumber < 12 order by incident.groupNumber desc")
    List<Incident> getRecentTwelve(Long currentGroupNumber);

//    @Query("select incident from Incident incident where incident.incidentTime ")
//    List<Incident> getBatched();
}
