package com.mixmate.domain.participant.dto;

import com.mixmate.domain.participant.enums.Round;

import java.util.List;

public record RosterRound(
        Round round,
        boolean assigned,
        List<RosterMember> members
) {
}
