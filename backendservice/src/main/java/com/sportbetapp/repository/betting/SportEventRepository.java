package com.sportbetapp.repository.betting;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.Wager;

@Repository
public interface SportEventRepository extends CrudRepository<SportEvent, Long> {

    Page<SportEvent> findAll(Pageable pageable);


}