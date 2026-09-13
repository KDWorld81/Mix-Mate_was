package com.mixmate.domain.assignment.service;

import com.mixmate.domain.assignment.entity.TeamAssignmentMember;
import com.mixmate.domain.assignment.repository.GroupAssignmentRepository;
import com.mixmate.domain.assignment.repository.TeamAssignmentMemberRepository;
import com.mixmate.domain.group.entity.Group;
import com.mixmate.domain.participant.enums.Round;
import com.mixmate.domain.participant.service.TeamNumberLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 편성 결과를 조 단위가 아니라 참가자 단위로 뒤집어 내줍니다.
 * 명단처럼 참가자 목록이 먼저 있고 거기에 조 번호만 붙이면 되는 호출부를 위한 것입니다.
 */
@Service
@RequiredArgsConstructor
public class TeamNumberReader implements TeamNumberLookup {

    private final GroupAssignmentRepository groupAssignmentRepository;
    private final TeamAssignmentMemberRepository teamAssignmentMemberRepository;

    /**
     * (assignment_id, participant_id) 유니크 제약이 있어 한 참가자는 한 배치에 한 번만 들어간다.
     * 그래서 toMap이 키 충돌로 터질 일이 없다.
     */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> teamNumbersOf(Group group, Round round) {
        List<TeamAssignmentMember> members = groupAssignmentRepository.findByGroupAndRound(group, round)
                .map(teamAssignmentMemberRepository::findByAssignment)
                .orElseGet(List::of);

        return members.stream()
                .collect(Collectors.toMap(
                        member -> member.getParticipant().getParticipantId(),
                        TeamAssignmentMember::getTeamNumber));
    }
}
