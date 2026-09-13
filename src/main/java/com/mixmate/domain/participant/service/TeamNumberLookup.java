package com.mixmate.domain.participant.service;

import com.mixmate.domain.group.entity.Group;
import com.mixmate.domain.participant.enums.Round;

import java.util.Map;

/**
 * 참가자가 그 차수에서 몇 조인지 알아오는 통로입니다.
 * 구현은 assignment 도메인에 있습니다. 선언을 participant에 두어 두 도메인이 서로를 참조하지 않게 합니다.
 */
public interface TeamNumberLookup {

    /** 참가자 id를 조 번호에 대응시켜 돌려줍니다. 해당 차수의 편성이 없으면 빈 Map입니다. */
    Map<Long, Integer> teamNumbersOf(Group group, Round round);
}
