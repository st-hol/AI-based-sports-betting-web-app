package com.sportbetapp.repository.betting;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.PointsTurnoverStatistic;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.betting.PointsTurnoverStatisticDto;

@Repository
public interface PointsTurnoverStatisticRepository extends CrudRepository<PointsTurnoverStatistic, Long>,
        JpaSpecificationExecutor<PointsTurnoverStatistic> {

    @Query("select new com.sportbetapp.dto.betting.PointsTurnoverStatisticDto(" +
            "st.id, st.wager.user.id, st.wager.user.name, st.wager.user.email, " +
            "SUM((CASE WHEN st.isWin = false THEN st.amount ELSE 0 END)), " + // wasted all
            "SUM((CASE WHEN st.isWin = true THEN st.amount ELSE 0 END)), " + // won all
            "(SUM((CASE WHEN st.isWin = true THEN st.amount ELSE 0 END)) - SUM((CASE WHEN st.isWin = false THEN st.amount ELSE 0 END))), " +
            "st.wager.user.currency, " +
            "(SUM((CASE WHEN st.isWin = true THEN st.amount ELSE 0 END)) > SUM((CASE WHEN st.isWin = false THEN st.amount ELSE 0 END)))) " +
            " from PointsTurnoverStatistic st" +
            " group by st.wager.user.id")
    Page<PointsTurnoverStatisticDto> findAllAggregated(Pageable pageable);

    void deleteByWagerId(Long idWager);

    @Query("select sum(st.amount) from PointsTurnoverStatistic st " +
            "where st.wager.user = :user " +
            "and st.isWin = true")
    BigDecimal sumAllWonByUser(User user);

    @Query("select sum(st.amount) from PointsTurnoverStatistic st " +
            "where st.wager.user = :user " +
            "and st.isWin = false")
    BigDecimal sumAllWastedByUser(User user);
}
