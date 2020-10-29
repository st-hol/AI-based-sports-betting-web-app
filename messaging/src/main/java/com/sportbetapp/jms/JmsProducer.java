package com.sportbetapp.jms;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.sportbetapp.dto.SportEventMessageDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JmsProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${ai.bet.app.activemq.topic}")
    private String topic;

    public void sendMessage(SportEventMessageDto sportEventMessageDto) {
        try {
            long delay = measureDelayToEndDate(sportEventMessageDto);

            log.info("Attempting Send SportEventMessageDto to Topic: " + topic);
            jmsTemplate.convertAndSend(topic, sportEventMessageDto, message -> {
                message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                return message;
            });
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    private long measureDelayToEndDate(SportEventMessageDto sportEventMessageDto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventEnd = LocalDateTime.of(sportEventMessageDto.getEndDate(),
                LocalTime.now());
//        LocalDateTime eventEnd = now.minusSeconds(5);
        return ChronoUnit.SECONDS.between(now, eventEnd);
    }
}
