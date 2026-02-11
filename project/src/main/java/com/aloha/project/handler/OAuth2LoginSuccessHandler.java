package com.aloha.project.handler;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.aloha.project.dto.CustomUser;
import com.aloha.project.dto.User;
import com.aloha.project.dto.UserSocial;
import com.aloha.project.service.UserSocialService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserSocialService userSocialService;

    // 생성자 주입
    public OAuth2LoginSuccessHandler(UserSocialService userSocialService) {
        this.userSocialService = userSocialService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("========== OAuth2 로그인 성공 ==========");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String socialId = oAuth2User.getAttribute("id").toString();
        String provider = "kakao";

        User user;

        try {
            // social_user → users 조회
            UserSocial social = new UserSocial();
            social.setProvider(provider);
            social.setSocialId(socialId);

            user = userSocialService.selectBySocial(social);

        } catch (Exception e) {
            log.error("소셜 사용자 조회 실패", e);
            response.sendRedirect("/login?error");
            return;
        }

        // CustomUser로 감싸기
        CustomUser customUser = new CustomUser(user);

        // Authentication 재생성 및 SecurityContext에 등록
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 세션에도 User 저장
        request.getSession().setAttribute("user", user);

        // 리다이렉트 설정
        setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authToken);
    }
}