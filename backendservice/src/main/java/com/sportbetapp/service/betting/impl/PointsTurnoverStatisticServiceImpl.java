package com.sportbetapp.service.betting.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sportbetapp.domain.betting.PointsTurnoverStatistic;
import com.sportbetapp.dto.betting.PointsTurnoverStatisticDto;
import com.sportbetapp.repository.betting.PointsTurnoverStatisticRepository;
import com.sportbetapp.service.betting.PointsTurnoverStatisticService;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.specification.PointsTurnoverStatisticSpecification;
import com.sportbetapp.specification.SearchCriteria;

@Service
public class PointsTurnoverStatisticServiceImpl implements PointsTurnoverStatisticService {

    @Autowired
    private PointsTurnoverStatisticRepository repository;
    @Autowired
    private UserService userService;


    @Override
    public List<PointsTurnoverStatistic> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public PointsTurnoverStatistic findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public PointsTurnoverStatistic save(PointsTurnoverStatistic statistic) {
        return repository.save(statistic);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Page<PointsTurnoverStatisticDto> findTopUsersSortedPageable(String search, Pageable pageable) {
        if (!search.isEmpty()) {
            if (search.equals("findme")) {
                search = userService.obtainCurrentPrincipleUser().getEmail();
            }
            PointsTurnoverStatisticSpecification spec1 =
                    new PointsTurnoverStatisticSpecification(new SearchCriteria("name", search, ":"));
            PointsTurnoverStatisticSpecification spec2 =
                    new PointsTurnoverStatisticSpecification(new SearchCriteria("email", search, ":"));

            Specification<PointsTurnoverStatistic> spec = Specification.where(spec1).or(spec2);

            Page<PointsTurnoverStatistic> allStats = repository.findAll(spec, pageable);
            return aggregateStatsByUsers(allStats);
        }

        return repository.findAllAggregated(pageable);
    }

    private Page<PointsTurnoverStatisticDto> aggregateStatsByUsers(Page<PointsTurnoverStatistic> allStats) {
        return allStats.map(this::fromEntity);
    }

    private PointsTurnoverStatisticDto fromEntity(PointsTurnoverStatistic entity) {
        PointsTurnoverStatisticDto dto = new  PointsTurnoverStatisticDto();
        dto.setId(entity.getId());
        dto.setUserName(entity.getWager().getUser().getName());
        dto.setUserEmail(entity.getWager().getUser().getEmail());
        dto.setCurrency(entity.getCurrency());

        dto.setWonAll(entity.getIsWin() ? entity.getAmount() : BigDecimal.ZERO);
        dto.setWastedAll(entity.getIsWin() ? BigDecimal.ZERO : entity.getAmount());
        dto.setDifference(entity.getIsWin() ? entity.getAmount() : entity.getAmount().negate());
        dto.setIsProfitable(entity.getIsWin());

//        BigDecimal allWonByUser = repository.sumAllWonByUser(entity.getWager().getUser());
//        BigDecimal allWastedByUser = repository.sumAllWastedByUser(entity.getWager().getUser());
//        BigDecimal diff = BigDecimalUtils.subtract(allWonByUser, allWastedByUser);
//
//        dto.setWonAll(allWonByUser);
//        dto.setWastedAll(allWastedByUser);
//        dto.setDifference(diff);

        return dto;
    }

    @Override
    public void deleteByWagerId(Long idWager) {
        repository.deleteByWagerId(idWager);
    }
}
