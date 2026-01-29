package com.aloha.project.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.project.dto.HotelRoom;
import com.aloha.project.dto.HotelService;
import com.aloha.project.service.HotelRoomService;
import com.aloha.project.service.HotelServiceService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final HotelRoomService hotelRoomService;       // 객실 서비스 DI
    private final HotelServiceService hotelServiceService; // 호텔 서비스 DI

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 예약 목록 페이지
    @GetMapping("/pet/reservation")
    public String reservation(
            Model model,
            @RequestParam(name="sort", required=false, defaultValue="default") String sort,
            @RequestParam(name="sizeType", required=false, defaultValue="all") String sizeType,
            @RequestParam(name="status", required=false, defaultValue="all") String status) {

        // DB에서 전체 객실 가져오기
        List<HotelRoom> rooms = hotelRoomService.getAllRooms();

        // 강아지 타입 필터링
        if (!"all".equals(sizeType)) {
            rooms = rooms.stream()
                    .filter(r -> r.getEtc().contains(sizeType))
                    .collect(Collectors.toList());
        }

        // 예약 상태 필터링
        if (!"all".equals(status)) {
            rooms = rooms.stream()
                    .filter(r -> r.getActive().equals(status))
                    .collect(Collectors.toList());
        }

        // 가격순 정렬
        if ("priceAsc".equals(sort)) {
            rooms = rooms.stream()
                    .sorted(Comparator.comparingInt(HotelRoom::getRoomPrice))
                    .collect(Collectors.toList());
        } else if ("priceDesc".equals(sort)) {
            rooms = rooms.stream()
                    .sorted(Comparator.comparingInt(HotelRoom::getRoomPrice).reversed())
                    .collect(Collectors.toList());
        }

        model.addAttribute("rooms", rooms);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("selectedSizeType", sizeType);
        model.addAttribute("selectedStatus", status);

        return "pet/reservation";
    }

    // 예약 상세 페이지 (roomNo 기준)
    @GetMapping("/pet/reservation/{roomNo}")
    public String reservationDetail(@PathVariable Long roomNo, Model model) {

        // DB에서 roomNo 기준 객실 가져오기
        HotelRoom selectedRoom = hotelRoomService.getRoom(roomNo);

        if (selectedRoom == null) {
            return "redirect:/pet/reservation"; // 없으면 리스트로
        }

        model.addAttribute("room", selectedRoom);

        // 호텔 서비스 목록 추가
        List<HotelService> roomServiceList = hotelServiceService.getAllServices();
        model.addAttribute("roomServiceList", roomServiceList);

        return "pet/reservation-detail";
    }
}
