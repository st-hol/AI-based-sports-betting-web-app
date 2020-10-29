package com.sportbetapp.dto.betting;

import java.util.List;

import lombok.Data;

@Data
public class ProcessResultDto {
    private Long id;
    private List<OutcomeDto> winnerOutcomes;
    private Long sportEventId;
}
