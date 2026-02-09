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

    // 1️⃣ 마이페이지 뷰 반환 (동기식)
/*     @GetMapping("/mypage")
    public String mypage() {
        return "mypage/mypage";
    }        */
    //maincontroller랑 충돌나서 잠시 주석 해놨습니다

    // 로그인한 사용자 정보 반환 (비동기식 JSON)
    @GetMapping("/api/users/me")
    @ResponseBody
    public User getCurrentUser(@AuthenticationPrincipal CustomUser customUser) {
        if (customUser != null) {
            log.info("로그인 사용자: " + customUser.getUsername());
            return customUser.getUser(); // JSON 반환
        } else {
            log.info("로그인 사용자 없음");
            return null; // 필요하면 예외 처리 가능
        }
    }


}