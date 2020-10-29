package com.sportbetapp.repository.technical;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.technical.ParametersArea;


@Repository
public interface ParametersAreaRepository extends CrudRepository<ParametersArea, String> {
}
