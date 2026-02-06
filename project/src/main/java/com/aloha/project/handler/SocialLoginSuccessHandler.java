/* package com.aloha.project.handler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.aloha.project.dto.CustomUser;
import com.aloha.project.dto.User;
import com.aloha.project.dto.UserSocial;
import com.aloha.project.repository.UserRepository;
import com.aloha.project.repository.UserSocialRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserSocialRepository userSocialRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        try {
            String provider = "kakao";
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String socialId = authentication.getName();
            String nickname = oAuth2User.getAttribute("nickname");

            // Social 계정 조회
            UserSocial social = userSocialRepository.findByProviderAndSocialId(provider, socialId);
            User user;

            if (social == null) {
                // 신규 사용자 생성
                user = new User();
                user.setId(UUID.randomUUID().toString());
                user.setUsername(provider + "_" + socialId);
                user.setName(nickname);
                user = userRepository.save(user); // DB 저장

                // Social 계정 연동 저장
                social = new UserSocial();
                social.setUserNo(user.getNo());
                social.setProvider(provider);
                social.setSocialId(socialId);
                userSocialRepository.save(social);

                log.info("신규 사용자 등록: {}", user.getUsername());
            } else {
                user = userRepository.findById(social.getUserNo())
                        .orElseThrow(() -> new IllegalStateException("사용자 정보가 없습니다."));
            }

            // Spring Security 인증 처리
            CustomUser customUser = new CustomUser(user);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            response.sendRedirect("/"); // 로그인 후 이동
        } catch (Exception e) {
            log.error("카카오 로그인 처리 중 에러", e);
            response.sendRedirect("/login?error");
        }
    }
}
 */