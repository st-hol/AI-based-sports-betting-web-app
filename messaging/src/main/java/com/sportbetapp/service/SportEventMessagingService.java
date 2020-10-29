package com.sportbetapp.service;

import com.sportbetapp.dto.SportEventMessageDto;

public interface SportEventMessagingService {

    void onMessageReceivedPopulateUpcoming(SportEventMessageDto sportEventMessageDto);

}
