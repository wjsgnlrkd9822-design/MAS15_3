package com.aloha.project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.project.dto.UserSocial;
import com.aloha.project.service.UserSocialService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthApiController {

    private final UserSocialService userSocialService;

    /**
     * 카카오 로그인 사용자 정보 조회
     * [GET] - /api/oauth/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getOAuthUserInfo(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
                response.put("success", false);
                response.put("message", "OAuth 로그인 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String socialId = oAuth2User.getAttribute("id").toString();
            
            UserSocial searchParam = new UserSocial();
            searchParam.setProvider("kakao");
            searchParam.setSocialId(socialId);
            
            UserSocial userSocial = userSocialService.selectSocial(searchParam);
            
            response.put("success", true);
            response.put("user", userSocial);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("OAuth 사용자 정보 조회 실패", e);
            response.put("success", false);
            response.put("message", "서버 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 카카오 로그인 후 추가 정보 업데이트
     * [POST] - /api/oauth/update
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateOAuthUser(
            @RequestBody UserSocial userSocial, 
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
                response.put("success", false);
                response.put("message", "OAuth 로그인 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String socialId = oAuth2User.getAttribute("id").toString();
            
            // 현재 로그인한 사용자의 소셜 ID로 업데이트
            userSocial.setSocialId(socialId);
            userSocial.setProvider("kakao");
            
            int result = userSocialService.updateSocial(userSocial);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "정보 수정 완료");
            } else {
                response.put("success", false);
                response.put("message", "정보 수정 실패");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("OAuth 사용자 정보 업데이트 실패", e);
            response.put("success", false);
            response.put("message", "서버 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 카카오 계정 연동 해제
     * [POST] - /api/oauth/unlink
     */
    @PostMapping("/unlink")
    public ResponseEntity<Map<String, Object>> unlinkOAuth(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
                response.put("success", false);
                response.put("message", "OAuth 로그인 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String socialId = oAuth2User.getAttribute("id").toString();
            
            UserSocial searchParam = new UserSocial();
            searchParam.setProvider("kakao");
            searchParam.setSocialId(socialId);
            
            UserSocial userSocial = userSocialService.selectSocial(searchParam);
            
            response.put("success", true);
            response.put("message", "카카오 계정 연동 해제 완료");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("OAuth 계정 연동 해제 실패", e);
            response.put("success", false);
            response.put("message", "서버 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}