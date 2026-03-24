package com.fitness.aiservice.entity;


import com.fitness.aiservice.enums.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Activity {
    private String id;
    private String userId;
    private String type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrices;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
