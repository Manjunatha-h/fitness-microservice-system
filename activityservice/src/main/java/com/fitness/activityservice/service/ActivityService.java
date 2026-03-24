package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.entity.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor // --> creates contructor only for the fileds wih @Notnull or "final" keyword
//@AllArgsConstructor --> it will create contructor for all the fields in the class
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest request) {
        Boolean isUser = userValidationService.validateUser(request.getUserId());
        if(!isUser){
            throw new RuntimeException("Invalid User " + request.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrices(request.getAdditionalMetrices())
                .build();
        Activity savedActivity = activityRepository.save(activity);

        // publish to rabbitMQ for AI processing

        try{
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
        }catch(Exception e){
            log.error("failed to publish activity to rabbitMQ",e);
        }
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setAdditionalMetrices(activity.getAdditionalMetrices());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());
        return activityResponse;
    }

    
    public List<ActivityResponse> getAllActivitiesOfUser(String userId) {
        List<Activity>  activities = activityRepository.findByUserId(userId);

        List<ActivityResponse> collect = activities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return collect;
    }

    public ActivityResponse findByActivityId(String activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("activity not found with activityId" + activityId));

        return mapToResponse(activity);
    }
}
