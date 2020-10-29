package com.sportbetapp.repository.technical.messaging;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.technical.messaging.UpcomingEventToPredict;


@Repository
public interface UpcomingEventToPredictRepository extends CrudRepository<UpcomingEventToPredict, Long> {
}
