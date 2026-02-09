package com.aloha.project.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aloha.project.dto.CustomUser;
import com.aloha.project.dto.HotelRoom;
import com.aloha.project.dto.Pet;
import com.aloha.project.dto.ReservationDto;
import com.aloha.project.service.HotelRoomService;
import com.aloha.project.service.HotelServiceService;
import com.aloha.project.service.PetService;
import com.aloha.project.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PetService petService;
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

    // 🔹 견종 필터
    if (!"all".equals(sizeType)) {
        rooms = rooms.stream()
                .filter(r -> r.getEtc().contains(sizeType))
                .collect(Collectors.toList());
    }

    // 🔹 예약 상태 필터
    if (!"all".equals(status)) {
        rooms = rooms.stream()
                .filter(r -> r.getActive().equals(status))
                .collect(Collectors.toList());
    }

    // 🔹 가격 정렬
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

    // 🔥 화면에 데이터 전달
    model.addAttribute("rooms", rooms);

    // 🔥🔥🔥 선택한 필터값 다시 전달 (이게 핵심)
    model.addAttribute("selectedSort", sort);
    model.addAttribute("selectedSizeType", sizeType);
    model.addAttribute("selectedStatus", status);

    return "pet/reservation";
}


    /**
     * 예약 상세 페이지
     */
    @GetMapping("/pet/reservation/{roomNo}")
    public String reservationDetail(
            @PathVariable("roomNo") Long roomNo,
            Model model,
            @AuthenticationPrincipal CustomUser customUser
    ) throws Exception {

        HotelRoom room = hotelRoomService.getRoom(roomNo);
        if (room == null) return "redirect:/pet/reservation";

        model.addAttribute("room", room);
        model.addAttribute("roomServiceList", hotelServiceService.getAllServices());

        LocalDate today = LocalDate.now();
        model.addAttribute("checkin", today.toString()); 
        model.addAttribute("checkout", today.plusDays(1).toString());

        if ( customUser != null ) {
            Long ownerNo = customUser.getNo();
            List<Pet> pets = petService.selectPetsByOwnerNo(ownerNo);
            model.addAttribute("pets", pets);
        }

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
                @RequestParam("petNo") Long petNo,
                @RequestParam(value="serviceIds", required=false) List<Long> serviceIds,
                @AuthenticationPrincipal CustomUser customUser,
                RedirectAttributes redirectAttributes
        ) {
            if (customUser == null) return "redirect:/login";

            Long userNo = customUser.getNo();

            LocalDate checkinDate = LocalDate.parse(checkin);
            LocalDate checkoutDate = LocalDate.parse(checkout);
            LocalTime resTime = LocalTime.now();

            // 객실 가격 계산
            int roomPrice = hotelRoomService.getRoom(roomNo).getRoomPrice();
            int nights = (int) ChronoUnit.DAYS.between(checkinDate, checkoutDate);

            // 서비스 가격 합산
            int serviceTotal = 0;
            if (serviceIds != null) {
                for (Long serviceNo : serviceIds) {
                    serviceTotal += reservationService.getServicePrice(serviceNo);
                }
            }

            int totalPrice = roomPrice * nights + serviceTotal;

            // 예약 DB 저장 + 선택 서비스 저장
            reservationService.insert(userNo, petNo, roomNo, checkinDate, checkoutDate, resTime, totalPrice, serviceIds);

            redirectAttributes.addFlashAttribute("checkin", checkin);
            redirectAttributes.addFlashAttribute("checkout", checkout);
            redirectAttributes.addFlashAttribute("nights", nights);
            redirectAttributes.addFlashAttribute("total", totalPrice);

            return "redirect:/mypage";
        }

    /**
     * 마이페이지
     */
   @GetMapping("/mypage")
public String mypage(Model model, @AuthenticationPrincipal CustomUser customUser) throws Exception {
    if(customUser != null){
        Long ownerNo = customUser.getNo();

        // 반려견 목록
        List<Pet> pets = petService.selectPetsByOwnerNo(ownerNo);
        model.addAttribute("pets", pets);

        // 예약 목록
        List<ReservationDto> reservations = reservationService.getReservationsByUser(ownerNo);
        model.addAttribute("reservations", reservations);
    }
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
            @RequestParam("total") int total,
            @RequestParam("totalPrice") int totalPrice,
            @RequestParam(value="serviceIds", required=false) List<Long> serviceIds
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            LocalDate checkinDate = LocalDate.parse(checkin);
            LocalDate checkoutDate = LocalDate.parse(checkout);

            // 서비스 포함 예약 업데이트
            reservationService.update(resNo, checkinDate, checkoutDate, total, totalPrice, serviceIds);

            result.put("success", true);
            result.put("message", "예약이 수정되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "수정 실패: " + e.getMessage());
        }
        return result;
    }

    // 예약 삭제 (AJAX용)
        @DeleteMapping("/api/reservation/delete/{resNo}")
        @ResponseBody
        public Map<String, Object> deleteReservation(@PathVariable("resNo") Long resNo) {
            Map<String, Object> result = new HashMap<>();
            try {
                reservationService.delete(resNo);
                result.put("success", true);
                result.put("message", "예약이 삭제되었습니다.");
            } catch (Exception e) {
                e.printStackTrace(); // ✅ 로그 찍기
                result.put("success", false);
                result.put("message", "삭제 실패: " + e.getMessage());
            }
            return result;
        }
            
            
            
            
            @GetMapping("/api/room/services")
            @ResponseBody
            public List<Map<String, Object>> getAllServices() {
                return hotelServiceService.getAllServices()
                    .stream()
                    .map(s -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("serviceNo", s.getServiceNo());
                        map.put("serviceName", s.getServiceName());
                        map.put("price", s.getServicePrice());
                        return map;
                    })
                    .collect(Collectors.toList());
            }



            /* insert 전용 */
            @PostMapping("/pet/reservation/insert/{roomNo}")
public String insertReservation(
        @PathVariable("roomNo") Long roomNo,
        @RequestParam("checkin") String checkin,
        @RequestParam("checkout") String checkout,
        @RequestParam("petNo") Long petNo,
        @RequestParam(value="serviceIds", required=false) List<Long> serviceIds,
        @AuthenticationPrincipal CustomUser customUser,
        RedirectAttributes redirectAttributes
) {
    if(customUser == null) return "redirect:/login";

    Long userNo = customUser.getNo();
    LocalDate checkinDate = LocalDate.parse(checkin);
    LocalDate checkoutDate = LocalDate.parse(checkout);
    LocalTime resTime = LocalTime.now();

    // 객실 가격 계산
    int roomPrice = hotelRoomService.getRoom(roomNo).getRoomPrice();
    int nights = (int) ChronoUnit.DAYS.between(checkinDate, checkoutDate);

    // 서비스 가격 합산
    int serviceTotal = 0;
    if(serviceIds != null) {
        for(Long serviceNo : serviceIds) {
            serviceTotal += reservationService.getServicePrice(serviceNo);
        }
    }

    int totalPrice = roomPrice * nights + serviceTotal;

    // DB 저장
    reservationService.insert(userNo, petNo, roomNo, checkinDate, checkoutDate, resTime, totalPrice, serviceIds);

    redirectAttributes.addFlashAttribute("checkin", checkin);
    redirectAttributes.addFlashAttribute("checkout", checkout);
    redirectAttributes.addFlashAttribute("nights", nights);
    redirectAttributes.addFlashAttribute("total", totalPrice);

    return "redirect:/mypage";
}

            
    }
// 컨트롤러 reservationservice.java, ReservationServiceImple.java, ReservationMapper.java ReservationMapper.xml, myPage.html