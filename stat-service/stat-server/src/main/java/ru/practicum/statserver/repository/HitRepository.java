package ru.practicum.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT COUNT(*) as hits, app, uri " +
            "FROM hit " +
            "WHERE uri IN (:uris) " +
            "AND hit_time BETWEEN :start AND :end " +
            "GROUP BY uri, app " +
            "ORDER BY hits DESC", nativeQuery = true)
    List<ViewStatsDto> countSelectedUriAllIpHits(@Param("uris") List<String> uris,
                                                 @Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);

    @Query(value = "SELECT COUNT(ip) as hits, uri, app " +
            "FROM (SELECT COUNT(*), app, uri, ip " +
            "      FROM hit " +
            "      WHERE uri IN (:uris) " +
            "        AND hit_time BETWEEN :start AND :end " +
            "      GROUP BY ip, uri, app) as H " +
            "GROUP BY uri, app " +
            "ORDER BY hits DESC", nativeQuery = true)
    List<ViewStatsDto> countSelectedUriUniqueIpHits(@Param("uris") List<String> uris,
                                                    @Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);

    @Query(value = "SELECT COUNT(*) as hits, app, uri " +
            "FROM hit " +
            "WHERE  hit_time BETWEEN :start AND :end " +
            "GROUP BY uri, app " +
            "ORDER BY hits DESC", nativeQuery = true)
    List<ViewStatsDto> countAllUriAllIpHits(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    @Query(value = "SELECT COUNT(ip) as hits, uri, app " +
            "FROM (SELECT COUNT(*), app, uri, ip " +
            "      FROM hit " +
            "      WHERE hit_time BETWEEN :start AND :end " +
            "      GROUP BY ip, uri, app) as H " +
            "GROUP BY uri, app " +
            "ORDER BY hits DESC", nativeQuery = true)
    List<ViewStatsDto> countAllUriUniqueIpHits(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);
}
