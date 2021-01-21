package com.sportbetapp.service.betting.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.service.betting.SportTypeService;

@Service
public class SportTypeServiceImpl implements SportTypeService {
    @Override
    public List<SportType> getAllSportTypes() {
        return Arrays.asList(SportType.values());
    }
}
