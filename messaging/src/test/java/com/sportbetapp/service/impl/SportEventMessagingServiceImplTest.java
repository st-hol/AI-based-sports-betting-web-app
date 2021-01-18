package com.sportbetapp.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.dto.SportEventMessageDto;
import com.sportbetapp.repository.betting.SportEventRepository;
import com.sportbetapp.repository.technical.messaging.UpcomingEventToPredictRepository;

@RunWith(MockitoJUnitRunner.class)
public class SportEventMessagingServiceImplTest {

    private static final long ID = 1L;

    @Mock
    private UpcomingEventToPredictRepository upcomingEventToPredictRepository;
    @Mock
    private SportEventRepository sportEventRepository;
    @InjectMocks
    private SportEventMessagingServiceImpl sportEventMessagingService;

    @Before
    public void setUp() {
        when(sportEventRepository.findById(any())).thenReturn(Optional.of(new SportEvent(){{
            setId(ID);
        }}));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowException_onMessageReceivedPopulateUpcoming_notFoundEvent() {
        //given
        when(sportEventRepository.findById(any())).thenReturn(Optional.empty());

        //when
        sportEventMessagingService.onMessageReceivedPopulateUpcoming(prepareInput());

        //then
        verifyZeroInteractions(upcomingEventToPredictRepository);
    }

    @Test
    public void saveEventUpcoming_onMessageReceivedPopulateUpcoming() {
        //when
        sportEventMessagingService.onMessageReceivedPopulateUpcoming(prepareInput());

        //then
        verify(upcomingEventToPredictRepository, times(1)).save(any());
    }

    private SportEventMessageDto prepareInput() {
        return SportEventMessageDto
                .builder()
                .id(ID)
                .build();
    }
}
