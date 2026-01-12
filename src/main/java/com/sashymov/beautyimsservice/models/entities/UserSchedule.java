package com.sashymov.beautyimsservice.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Table(name = "user_schedule")
@Data
public class UserSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime endTime;

}
