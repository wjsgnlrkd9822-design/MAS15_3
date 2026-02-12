package com.aloha.project.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloha.project.dto.CustomUser;
import com.aloha.project.dto.User;
import com.aloha.project.service.HotelRoomService;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MyPageController {

    private final HotelRoomService hotelRoomService;

    MyPageController(HotelRoomService hotelRoomService) {
        this.hotelRoomService = hotelRoomService;
    }

    // 로그인한 사용자 정보 반환 (비동기식 JSON)
    @GetMapping("/api/users/me")
    @ResponseBody
    public User getCurrentUser(@AuthenticationPrincipal CustomUser customUser) {
        if (customUser != null) {
            log.info("로그인 사용자: " + customUser.getUsername());
            return customUser.getUser(); 
        } else {
            log.info("로그인 사용자 없음");
            return null;
        }
    }


}