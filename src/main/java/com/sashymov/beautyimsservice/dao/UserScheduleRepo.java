package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserScheduleRepo extends JpaRepository<UserSchedule, Long> {
    @Query("""
       SELECT COUNT(s) > 0 FROM UserSchedule s
       WHERE s.user.id = :userId
         AND :start < s.endTime
         AND :end > s.startTime
    """)
    boolean existsOverlappingSlot(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
