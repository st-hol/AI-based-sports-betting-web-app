package com.sportbetapp.repository.betting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.user.User;


@Repository
public interface WagerRepository extends CrudRepository<Wager, Long> {

    List<Wager> findAllByUser(User user);
}
