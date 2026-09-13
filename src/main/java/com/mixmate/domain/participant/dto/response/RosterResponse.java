package com.mixmate.domain.participant.dto.response;

import com.mixmate.domain.participant.dto.RosterRound;

import java.util.List;

public record RosterResponse(
        String groupName, List<RosterRound> rounds
) {
}
