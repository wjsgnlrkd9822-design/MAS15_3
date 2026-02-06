package com.aloha.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.project.dto.UserSocial;

@Mapper
public interface UserSocialMapper {

    UserSocial selectByProviderAndSocialId(
        @Param("provider") String provider,
        @Param("socialId") String socialId
    );

    UserSocial selectByUserNo(@Param("userNo") Long userNo);

    int insert(UserSocial userSocial);

    int deleteByUserno(@Param("userno") Long userNo);
    
}
