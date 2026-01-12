package com.sashymov.beautyimsservice.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "user_working_hours")
@Data
public class UserWorkingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime endTime;
}

