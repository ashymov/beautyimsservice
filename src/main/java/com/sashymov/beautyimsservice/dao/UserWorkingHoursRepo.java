package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.UserWorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserWorkingHoursRepo extends JpaRepository<UserWorkingHours, Long> {
    List<UserWorkingHours> findAllByUserIdAndDayOfWeek(Long userId,DayOfWeek dayOfWeek);
    Optional<UserWorkingHours> findByUserIdAndDayOfWeek(Long userId, DayOfWeek dayOfWeek);
    List<UserWorkingHours> findAllByUserId(Long userId);


}
