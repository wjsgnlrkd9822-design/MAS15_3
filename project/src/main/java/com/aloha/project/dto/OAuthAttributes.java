package com.aloha.project.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;      // OAuth 토큰 속성들
    private String nameAttributeKey;             // 사용자 이름 속성 키
    private String name;                         // 이름(닉네임)
    private String email;                        // 이메일
    private String socialId;                     // 소셜 ID (kakao의 경우 id)
    private String provider;                     // 소셜 제공자 (kakao, naver 등)

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, 
                          String name, String email, String socialId, String provider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.socialId = socialId;
        this.provider = provider;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, 
                                     Map<String, Object> attributes) {
        if("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        
        return null;
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        log.info("ofKakao attributes: {}", attributes);
        
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        
        log.info("kakaoAccount: {}", kakaoAccount);
        log.info("profile: {}", profile);

        return OAuthAttributes.builder()
                              .name((String) profile.get("nickname"))
                              .email((String) kakaoAccount.get("email"))
                              .socialId(String.valueOf(attributes.get(userNameAttributeName)))
                              .provider("kakao")
                              .attributes(attributes)
                              .nameAttributeKey(userNameAttributeName)
                              .build();
    }
    
    /**
     * OAuthAttributes를 UserSocial DTO로 변환
     */
    public UserSocial toEntity(Long userNo, String username) {
        UserSocial userSocial = new UserSocial();
        userSocial.setUserNo(userNo);           // 기존 사용자 번호
        userSocial.setUsername(username);        // 사용자명
        userSocial.setProvider(this.provider);   // kakao
        userSocial.setSocialId(this.socialId);   // 카카오 ID
        userSocial.setName(this.name);           // 닉네임
        userSocial.setEmail(this.email);         // 이메일
        
        return userSocial;
    }
    
    /**
     * 간단 버전 - userNo 없이 변환
     */
    public UserSocial toEntity() {
        return toEntity(null, null);
    }
}