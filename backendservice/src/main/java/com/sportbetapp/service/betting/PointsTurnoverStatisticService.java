package com.sportbetapp.service.betting;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sportbetapp.domain.betting.PointsTurnoverStatistic;
import com.sportbetapp.dto.betting.PointsTurnoverStatisticDto;

public interface PointsTurnoverStatisticService {
    List<PointsTurnoverStatistic> findAll();
    PointsTurnoverStatistic findById(Long id);
    PointsTurnoverStatistic save(PointsTurnoverStatistic statistic);
    void deleteAll();
    void deleteByWagerId(Long idWager);

    Page<PointsTurnoverStatisticDto> findTopUsersSortedPageable(String search, Pageable pageable);
}
