package com.aloha.project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.User;
import com.aloha.project.dto.UserSocial;

@Mapper
public interface UserSocialMapper {


    public UserSocial selectSocial(UserSocial userSocial) throws Exception;

    public int insertSocial(UserSocial userSocial) throws Exception;

    public int updateSocial(UserSocial userSocial) throws Exception;

    public User selectBySocial(UserSocial userSocial) throws Exception;


    
}
