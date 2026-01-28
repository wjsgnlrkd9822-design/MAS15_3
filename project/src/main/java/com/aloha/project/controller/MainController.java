package com.aloha.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.project.dto.HotelRoom; // ⚠️ 패키지명 프로젝트에 맞게 조정

@Controller
@RequestMapping // 루트 기준
public class MainController {

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 예약 페이지
    @GetMapping("/pet/reservation")
    public String reservation(Model model) {

        // ✅ 여러 객실 데이터를 임시 리스트로 생성
        List<HotelRoom> rooms = new ArrayList<>();

        rooms.add(new HotelRoom("101호", 110000, "대형견 이용 가능", "예약가능", "room_101.jpg"));
        rooms.add(new HotelRoom("102호", 110000, "대형견 이용 가능", "예약가능", "room_102.jpg"));
        rooms.add(new HotelRoom("103호", 110000, "대형견 이용 가능", "예약가능", "room_103.jpg"));
        rooms.add(new HotelRoom("104호", 140000, "대형견 이용 가능 / 넓은 공간", "예약가능", "room_104.jpg"));
        rooms.add(new HotelRoom("201호", 80000, "중형견 이용 가능", "예약가능", "room_201.jpg"));
        rooms.add(new HotelRoom("202호", 80000, "중형견 이용 가능", "예약가능", "room_202.jpg"));
        rooms.add(new HotelRoom("203호", 80000, "중형견 이용 가능", "예약가능", "room_203.jpg"));
        rooms.add(new HotelRoom("204호", 100000, "중형견 이용 가능 / 넓은 공간", "예약가능", "room_204.jpg"));
        rooms.add(new HotelRoom("205호", 100000, "중형견 이용 가능 / 넓은 공간", "예약가능", "room_205.jpg"));
        rooms.add(new HotelRoom("301호", 50000, "소형견 이용 가능", "예약가능", "room_301.jpg"));
        rooms.add(new HotelRoom("302호", 50000, "소형견 이용 가능", "예약가능", "room_302.jpg"));
        rooms.add(new HotelRoom("303호", 50000, "소형견 이용 가능", "예약가능", "room_303.jpg"));
        rooms.add(new HotelRoom("304호", 70000, "소형견 이용 가능 / 넓은 공간", "예약가능", "room_304.jpg"));
        rooms.add(new HotelRoom("305호", 70000, "소형견 이용 가능 / 넓은 공간", "예약가능", "room_305.jpg"));

        // ✅ Thymeleaf에 리스트 전달
        model.addAttribute("rooms", rooms);

        return "pet/reservation";
    }
}
