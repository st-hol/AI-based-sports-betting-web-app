package com.sportbetapp.service.betting.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sportbetapp.domain.type.BetType;
import com.sportbetapp.service.betting.BetTypeService;

@Service
public class BetTypeServiceImpl implements BetTypeService {
    @Override
    public List<BetType> getAllBetTypes() {
        return Arrays.asList(BetType.values());
    }
}
