package com.mixmate.domain.auth.dto.response;

import com.mixmate.domain.auth.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResDto {

    @Schema(example = "1")
    private final Long userId;

    @Schema(example = "hi@example.com")
    private final String email;

    @Schema(example = "hihello")
    private final String userName;

    @Schema(description = "가입 경로. 소셜 로그인 계정은 비밀번호가 없으므로, 프론트에서 이 값으로 "
            + "비밀번호 관련 UI(탈퇴 시 비밀번호 확인 모달 등)를 보여줄지 판단해야 함", example = "KAKAO")
    private final String provider;

    @Schema(example = "618hdjfnvs3jr1f....")
    private final String accessToken;

    @Schema(example = "618hdjfnvs3jr1f....")
    private final String refreshToken;

    public static LoginResDto fromEntity(User user, String accessToken, String refreshToken) {
        return LoginResDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .provider(user.getProvider().name())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
