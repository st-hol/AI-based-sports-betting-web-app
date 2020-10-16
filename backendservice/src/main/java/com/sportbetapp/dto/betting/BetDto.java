package com.sportbetapp.dto.betting;

import java.util.List;

import com.sportbetapp.domain.type.BetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetDto {
    private Long id;
    private Long sportEventId;
    private String description;
    private BetType type;
}
