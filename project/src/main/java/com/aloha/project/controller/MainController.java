package com.aloha.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.project.dto.HotelRoom; // ⚠️ 패키지명은 형님 프로젝트에 맞게 조정

@Controller
@RequestMapping   // 클래스 레벨 매핑 없음 (루트 기준)
public class MainController {

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 예약 페이지
    @GetMapping("/pet/reservation")
    public String reservation(Model model) {

        // ✅ 임시 객실 데이터 (에러 방지용)
        HotelRoom room = new HotelRoom();
        room.setRoomType("101호");
        room.setRoomPrice(110000);
        room.setImg("room_101.jpg");

        // ✅ Thymeleaf로 전달
        model.addAttribute("room", room);

        return "pet/reservation";
    }
}
