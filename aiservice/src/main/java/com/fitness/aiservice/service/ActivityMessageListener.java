package com.fitness.aiservice.service;

import com.fitness.aiservice.entity.Activity;
import com.fitness.aiservice.entity.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Activity RECIEVED for processing : {}" , activity.getId());
        log.info("RESPONSE FROM AI : {}",activityAiService.generateRecommendation(activity));

        recommendationRepository.save(activityAiService.generateRecommendation(activity));

    }
}
