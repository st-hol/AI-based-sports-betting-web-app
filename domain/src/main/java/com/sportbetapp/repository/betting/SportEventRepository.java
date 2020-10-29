package com.sportbetapp.repository.betting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.betting.SportEvent;

@Repository
public interface SportEventRepository extends CrudRepository<SportEvent, Long> {

    Page<SportEvent> findAll(Pageable pageable);


}