package com.statuspage.status.Repository;

import com.statuspage.status.model.StatusName;
import com.statuspage.status.model.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface WebsiteRepository extends JpaRepository<Website, UUID> {

    @Query("select website from Website website where website.url= :url")
    Optional<Website> findByUrl(String url);

    @Transactional
    @Modifying
    @Query("update Website website set website.currentStatus = ?1 where website.url = ?2")
    int updateWebsiteStatus(StatusName newStatus, String websiteUrl);


}
