package com.mixmate.domain.participant.dto;

import com.mixmate.domain.participant.entity.Participant;
import com.mixmate.domain.participant.entity.ParticipantProfile;
import com.mixmate.domain.participant.enums.Gender;
import com.mixmate.domain.participant.enums.Grade;

public record RosterMember(
        String studentId,
        String displayName,
        String major,
        Grade grade,
        Gender gender,
        Integer teamNumber
) {
    public static RosterMember of(Participant participant, Integer teamNumber) {
        ParticipantProfile profile = participant.getProfile();
        return new RosterMember(
                profile.getStudentId(),
                profile.getDisplayName(),
                profile.getMajor(),
                profile.getGrade(),
                profile.getGender(),
                teamNumber
        );
    }
}
