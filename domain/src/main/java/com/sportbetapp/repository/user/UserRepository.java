package com.sportbetapp.repository.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.user.User;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

//    @Query("select u from User u " +
//            "inner join Wager w on w.user = u " +
//            "inner join OutcomeOdd od on w.outcomeOdd = od " +
//            "inner join Outcome o on od.outcome = o " +
//            "where o = :outcome")
//    List<User> findAllByOutcome(@Param("outcome") Outcome outcome);
}
