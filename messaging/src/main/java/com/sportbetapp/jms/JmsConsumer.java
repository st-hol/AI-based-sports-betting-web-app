package com.sportbetapp.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.sportbetapp.dto.SportEventMessageDto;
import com.sportbetapp.service.SportEventMessagingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JmsConsumer implements MessageListener {

    @Autowired
    private SportEventMessagingService sportEventMessagingService;

    @Override
    @JmsListener(destination = "${ai.bet.app.activemq.topic}")
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            SportEventMessageDto sportEventMessageDto = (SportEventMessageDto) objectMessage.getObject();

            //do processing
            sportEventMessagingService.onMessageReceivedPopulateUpcoming(sportEventMessageDto);

            log.info("Received Message from Topic: " + sportEventMessageDto.toString());
        } catch (Exception e) {
            log.error("Received Exception while processing message: " + e);
        }

    }
}
