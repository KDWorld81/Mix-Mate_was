package com.mixmate.security;


import com.mixmate.exception.CustomAuthenticationEntryPoint;
import com.mixmate.exception.ErrorCode;
import com.mixmate.redis.RedisService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final RedisService redisService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            try {
                // 2. 블랙리스트 체크 (가장 먼저 수행!)

                if (redisService.getData("BLACKLIST:" + token) != null) {
                    // 블랙리스트에 있으면 다음 로직을 타지 않고 바로 예외 처리 지점으로 보냄
                    request.setAttribute("exception", ErrorCode.BLACKLIST_TOKEN);
                    filterChain.doFilter(request, response);
                    return;
                }

                // 3. 기존 토큰 검증 및 인증 로직
                String email = jwtUtil.getEmailFromToken(token);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
                // 토큰이 만료·파싱 실패한 경우뿐 아니라, 서명은 유효해도 그 사이 계정이 탈퇴되어
                // 더 이상 존재하지 않는 경우(예: 탈퇴 처리 후 만료 전 토큰으로 재요청)도 여기서 잡아
                // 인증 실패로 통일한다. 안 잡으면 UsernameNotFoundException이 필터를 그대로 뚫고
                // 나가 500으로 응답돼버린다.
                request.setAttribute("exception", ErrorCode.JWT_TOKEN_PARSING_ERROR);
            }
        }
        filterChain.doFilter(request, response);
    }
}
