package com.aloha.project.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
import com.aloha.project.dto.User;
import com.aloha.project.dto.UserSocial;
import com.aloha.project.service.HotelRoomService;
import com.aloha.project.service.HotelServiceService;
import com.aloha.project.service.PetService;
import com.aloha.project.service.ReservationService;
import com.aloha.project.service.UserService;
import com.aloha.project.service.UserSocialService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final PetService petService;
    private final HotelRoomService hotelRoomService;       
    private final HotelServiceService hotelServiceService; 
    private final ReservationService reservationService;
    private final UserService userService;
    private final UserSocialService userSocialService;

    /**
     * 로그인한 사용자의 No 가져오기 (일반 + OAuth2 통합)
     */
    private Long getUserNo(Authentication authentication) throws Exception {
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        // 일반 로그인
        if (principal instanceof CustomUser) {
            return ((CustomUser) principal).getNo();
        }
        
        // OAuth2 로그인
        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            String socialId = oAuth2User.getAttribute("id").toString();
            
            // UserSocial 조회
            UserSocial searchParam = new UserSocial();
            searchParam.setProvider("kakao");
            searchParam.setSocialId(socialId);
            
            UserSocial userSocial = userSocialService.selectSocial(searchParam);
            return userSocial != null ? userSocial.getUserNo() : null;
        }

        return null;
    }

    /**
     * 메인 페이지
     */
    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        boolean isLogin = authentication != null && authentication.isAuthenticated();
        model.addAttribute("isLogin", isLogin);
        return "index";
    }

    @GetMapping("/pet/introduce")
    public String service() {
        return "pet/introduce";
    }

    /**
     * 예약 페이지
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
            Model model,
            Authentication authentication
    ) throws Exception {

        HotelRoom room = hotelRoomService.getRoom(roomNo);
        if (room == null) return "redirect:/pet/reservation";

        model.addAttribute("room", room);
        model.addAttribute("roomServiceList", hotelServiceService.getAllServices());

        LocalDate today = LocalDate.now();
        model.addAttribute("checkin", today.toString()); 
        model.addAttribute("checkout", today.plusDays(1).toString());

        // 로그인한 사용자의 펫 목록 조회 (일반 + OAuth2)
        Long ownerNo = getUserNo(authentication);
        if (ownerNo != null) {
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
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        
        Long userNo = getUserNo(authentication);
        if (userNo == null) return "redirect:/login";

        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        LocalTime resTime = LocalTime.now();

        int roomPrice = hotelRoomService.getRoom(roomNo).getRoomPrice();
        int nights = (int) ChronoUnit.DAYS.between(checkinDate, checkoutDate);

        int serviceTotal = 0;
        if (serviceIds != null) {
            for (Long serviceNo : serviceIds) {
                serviceTotal += reservationService.getServicePrice(serviceNo);
            }
        }

        int totalPrice = roomPrice * nights + serviceTotal;

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
    public String mypage(Model model, Authentication authentication) throws Exception {
        
        Long ownerNo = getUserNo(authentication);  // ✅ 일반 + OAuth2 통합 처리
        
        if (ownerNo != null) {
            // 반려견 목록
            List<Pet> pets = petService.selectPetsByOwnerNo(ownerNo);
            model.addAttribute("pets", pets);

            // 예약 목록
            List<ReservationDto> reservations = reservationService.getReservationsByUser(ownerNo);
            model.addAttribute("reservations", reservations);
            
            // 사용자 정보도 추가 (필요시)
            User user = userService.selectByNo(ownerNo);
            model.addAttribute("user", user);
        }
        
        return "mypage/mypage";
    }

    @GetMapping("/api/reservation/{resNo}")
    @ResponseBody
    public ReservationDto getReservation(@PathVariable("resNo") Long resNo) {
        return reservationService.getReservationByResNo(resNo);
    }

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

            reservationService.update(resNo, checkinDate, checkoutDate, total, totalPrice, serviceIds);

            result.put("success", true);
            result.put("message", "예약이 수정되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "수정 실패: " + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/api/reservation/delete/{resNo}")
    @ResponseBody
    public Map<String, Object> deleteReservation(@PathVariable("resNo") Long resNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            reservationService.delete(resNo);
            result.put("success", true);
            result.put("message", "예약이 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
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

    @PostMapping("/pet/reservation/insert/{roomNo}")
    public String insertReservation(
            @PathVariable("roomNo") Long roomNo,
            @RequestParam("checkin") String checkin,
            @RequestParam("checkout") String checkout,
            @RequestParam("petNo") Long petNo,
            @RequestParam(value="serviceIds", required=false) List<Long> serviceIds,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        
        Long userNo = getUserNo(authentication);
        if (userNo == null) return "redirect:/login";

        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        LocalTime resTime = LocalTime.now();

        int roomPrice = hotelRoomService.getRoom(roomNo).getRoomPrice();
        int nights = (int) ChronoUnit.DAYS.between(checkinDate, checkoutDate);

        int serviceTotal = 0;
        if (serviceIds != null) {
            for (Long serviceNo : serviceIds) {
                serviceTotal += reservationService.getServicePrice(serviceNo);
            }
        }

        int totalPrice = roomPrice * nights + serviceTotal;

        reservationService.insert(userNo, petNo, roomNo, checkinDate, checkoutDate, resTime, totalPrice, serviceIds);

        redirectAttributes.addFlashAttribute("checkin", checkin);
        redirectAttributes.addFlashAttribute("checkout", checkout);
        redirectAttributes.addFlashAttribute("nights", nights);
        redirectAttributes.addFlashAttribute("total", totalPrice);

        return "redirect:/mypage";
    }
}