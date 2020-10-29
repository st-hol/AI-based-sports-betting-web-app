package com.sportbetapp.dto.betting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutcomeDto {
    private Long id;
    private Long betId;
    private String description;
}
