package com.fitness.aiservice.service;

import com.fitness.aiservice.entity.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAiService activityAiService;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Activity RECIEVED for processing : {}" , activity.getId());
        log.info("RESPONSE FROM AI : {}",activityAiService.generateRecommendation(activity));
    }
}
