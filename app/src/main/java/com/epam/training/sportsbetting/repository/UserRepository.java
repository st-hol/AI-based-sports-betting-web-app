package com.epam.training.sportsbetting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.user.User;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    @Query("select u from User u " +
            "inner join Wager w on w.player = u " +
            "inner join OutcomeOdd od on w.outcomeOdd = od " +
            "inner join Outcome o on od.outcome = o " +
            "where o = :outcome")
    List<User> findAllByOutcome(@Param("outcome") Outcome outcome);
}
