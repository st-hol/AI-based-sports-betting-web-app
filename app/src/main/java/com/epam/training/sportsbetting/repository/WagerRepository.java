package com.epam.training.sportsbetting.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epam.training.sportsbetting.domain.Wager;
import com.epam.training.sportsbetting.domain.user.User;


@Repository
public interface WagerRepository extends CrudRepository<Wager, Long> {

    List<Wager> findAllByPlayer(User player);
}
