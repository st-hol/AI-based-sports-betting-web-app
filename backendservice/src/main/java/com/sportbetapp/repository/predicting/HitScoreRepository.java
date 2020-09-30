package com.sportbetapp.repository.predicting;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.predicting.HitScore;


@Repository
public interface HitScoreRepository extends CrudRepository<HitScore, Long> {
}
