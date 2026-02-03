package com.aloha.project.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aloha.project.dto.HotelRoom;
import com.aloha.project.dto.ReservationDto;
import com.aloha.project.service.HotelRoomService;
import com.aloha.project.service.HotelServiceService;
import com.aloha.project.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final HotelRoomService hotelRoomService;       
    private final HotelServiceService hotelServiceService; 
    private final ReservationService reservationService;  

    /**
     * 메인 페이지
     * 로그인 여부를 model에 전달
     */
    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        boolean isLogin = userDetails != null;
        model.addAttribute("isLogin", isLogin);
        return "index";
    }

    @GetMapping("/pet/introduce")
    public String service() {
        return "pet/introduce";
    }
    

    /**
     * 예약 페이지
     * 로그인 필요는 SecurityConfig에서 처리
     */
    @GetMapping("/pet/reservation")
    public String reservation(
            Model model,
            @RequestParam(value="sort", defaultValue="default") String sort,
            @RequestParam(value="sizeType", defaultValue="all") String sizeType,
            @RequestParam(value="status", defaultValue="all") String status
    ) {
        List<HotelRoom> rooms = hotelRoomService.getAllRooms();

        if (!"all".equals(sizeType)) {
            rooms = rooms.stream()
                    .filter(r -> r.getEtc().contains(sizeType))
                    .collect(Collectors.toList());
        }

        if (!"all".equals(status)) {
            rooms = rooms.stream()
                    .filter(r -> r.getActive().equals(status))
                    .collect(Collectors.toList());
        }

        if ("priceAsc".equals(sort)) {
            rooms = rooms.stream()
                    .sorted(Comparator.comparingInt(HotelRoom::getRoomPrice))
                    .collect(Collectors.toList());
        } 
        else if ("priceDesc".equals(sort)) {
            rooms = rooms.stream()
                    .sorted(Comparator.comparingInt(HotelRoom::getRoomPrice).reversed())
                    .collect(Collectors.toList());
        }

        model.addAttribute("rooms", rooms);
        return "pet/reservation";
    }

    /**
     * 예약 상세 페이지
     */
    @GetMapping("/pet/reservation/{roomNo}")
    public String reservationDetail(
            @PathVariable("roomNo") Long roomNo,
            Model model
    ) {
        HotelRoom room = hotelRoomService.getRoom(roomNo);
        if (room == null) return "redirect:/pet/reservation";

        model.addAttribute("room", room);
        model.addAttribute("roomServiceList", hotelServiceService.getAllServices());

        LocalDate today = LocalDate.now();
        model.addAttribute("checkin", today.toString()); 
        model.addAttribute("checkout", today.plusDays(1).toString());

        return "pet/reservation-detail";
    }

    /**
     * 예약 확인
     */
    @PostMapping("/pet/reservation/confirm/{roomNo}")
    public String confirmReservation(
            @PathVariable("roomNo") Long roomNo,
            @RequestParam("checkin") String checkin,
            @RequestParam("checkout") String checkout,
            @RequestParam("nights") int nights,
            @RequestParam("total") int total,
            RedirectAttributes redirectAttributes
    ) {
        System.out.println("=== 서버에서 받은 값 ===");
        System.out.println("체크인: " + checkin);
        System.out.println("체크아웃: " + checkout);
        System.out.println("박수: " + nights);
        
        Long userNo = 1L;
        Long petNo = 1L;

        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        LocalTime resTime = LocalTime.now();

        reservationService.insert(userNo, petNo, roomNo, checkinDate, checkoutDate, resTime);

        redirectAttributes.addFlashAttribute("checkin", checkin);
        redirectAttributes.addFlashAttribute("checkout", checkout);
        redirectAttributes.addFlashAttribute("nights", nights);
        redirectAttributes.addFlashAttribute("total", total);

        return "redirect:/mypage";
    }

    /**
     * 마이페이지
     */
    @GetMapping("/mypage")
    public String mypage(Model model) {
        Long userNo = 1L;

        List<ReservationDto> reservations = reservationService.getReservationsByUser(userNo);
        model.addAttribute("reservations", reservations);
        return "mypage/mypage";
    }

    // ✅ 예약 1건 조회 (AJAX용)
    @GetMapping("/api/reservation/{resNo}")
    @ResponseBody
    public ReservationDto getReservation(@PathVariable("resNo") Long resNo) {
        return reservationService.getReservationByResNo(resNo);
    }

    // ✅ 예약 수정 (AJAX용)
    @PostMapping("/api/reservation/update/{resNo}")
    @ResponseBody
    public Map<String, Object> updateReservation(
        @PathVariable("resNo") Long resNo,
        @RequestParam("checkin") String checkin,
        @RequestParam("checkout") String checkout,
        @RequestParam("total") int total
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            LocalDate checkinDate = LocalDate.parse(checkin);
            LocalDate checkoutDate = LocalDate.parse(checkout);
            
            reservationService.update(resNo, checkinDate, checkoutDate, total);
            
            result.put("success", true);
            result.put("message", "예약이 수정되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "수정 실패: " + e.getMessage());
        }
        return result;
    }
}

// 컨트롤러 reservationservice.java, ReservationServiceImple.java, ReservationMapper.java ReservationMapper.xml, myPage.html