package com.aloha.project.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.project.dto.HotelRoom;
import com.aloha.project.dto.HotelService;
import com.aloha.project.dto.MonthlySalesDto;
import com.aloha.project.dto.ReservationDto;
import com.aloha.project.dto.userTotalSales;
import com.aloha.project.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;

    @Override
    @Transactional
    public void insert(Long userNo, Long petNo, Long roomNo, LocalDate checkinDate, LocalDate checkoutDate, LocalTime resTime, int totalPrice, List<Long> serviceNos) {
        // ReservationDto 생성
        ReservationDto dto = new ReservationDto();
        dto.setUserNo(userNo);
        dto.setPetNo(petNo);
        dto.setRoomNo(roomNo);
        dto.setCheckin(checkinDate);
        dto.setCheckout(checkoutDate);
        dto.setResTime(resTime.toString());
        dto.setTotalPrice(totalPrice);
        dto.setNights((int) ChronoUnit.DAYS.between(checkinDate, checkoutDate));

        // insert → useGeneratedKeys로 resNo 자동 세팅
        int cnt = reservationMapper.insertReservation(dto);
        if (cnt != 1) {
            throw new RuntimeException("예약 추가 실패: userNo=" + userNo + ", petNo=" + petNo + ", roomNo=" + roomNo);
        }
        Long resNo = dto.getResNo();

        // 서비스 등록
        if(serviceNos != null && !serviceNos.isEmpty()) {
            for(Long serviceNo : serviceNos) {
                reservationMapper.insertReservationService(resNo, serviceNo);
            }
        }
    }

    @Override
    public List<ReservationDto> getReservationsByUser(Long userNo) {
        List<ReservationDto> reservations = reservationMapper.findByUserNo(userNo);

        for (ReservationDto res : reservations) {
            if (res.getCheckin() != null && res.getCheckout() != null) {
                long days = ChronoUnit.DAYS.between(res.getCheckin(), res.getCheckout());
                res.setNights((int) days);
            }
            res.setTotal(100000);
        }
        return reservations;
    }

    @Override
    public ReservationDto getReservationByResNo(Long resNo) {
        ReservationDto reservation = reservationMapper.findByResNo(resNo);
        if (reservation != null) {
            if (reservation.getCheckin() != null && reservation.getCheckout() != null) {
                long days = ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout());
                reservation.setNights((int) days);
            }
            
            List<Long> serviceIds = reservationMapper.selectServiceIdsByReservation(resNo);
            reservation.setServiceIds(serviceIds);
        }
        return reservation;
    }

    @Override
    @Transactional
    public void update(Long resNo, LocalDate checkinDate, LocalDate checkoutDate, int total, int totalPrice, List<Long> serviceNos) {
        int cnt = reservationMapper.update(resNo, checkinDate, checkoutDate, total, totalPrice);
        if (cnt != 1) {
            throw new RuntimeException("예약 수정 실패: resNo=" + resNo);
        }

        // 기존 서비스 삭제
        reservationMapper.deleteReservationServices(resNo);

        // 새로운 서비스 등록
        if(serviceNos != null && !serviceNos.isEmpty()) {
            for(Long serviceNo : serviceNos) {
                reservationMapper.insertReservationService(resNo, serviceNo);
            }
        }
    }

    @Override
    public void delete(Long resNo) {
        reservationMapper.deleteReservation(resNo);
    }

    @Override
    public int getServicePrice(Long serviceNo) {
        return reservationMapper.getServicePrice(serviceNo);
    }

    @Override
    public List<HotelService> getServicesByReservation(Long resNo) {
        return reservationMapper.selectServicesByReservation(resNo);
    }

    @Override
    public Long getTotalSales() {
        Long total = reservationMapper.getTotalSales();
        return (total == null) ? 0L : total;
    }

    // ⭐ 새로 추가: 날짜별 예약 관리 메서드 구현
    
    @Override
    public List<HotelRoom> getAvailableRooms(String roomType, LocalDate checkin, LocalDate checkout) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomType", roomType);
        params.put("checkin", checkin);
        params.put("checkout", checkout);
        
        return reservationMapper.getAvailableRooms(params);
    }
    
    @Override
    public boolean isRoomAvailable(Long roomNo, LocalDate checkin, LocalDate checkout) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomNo", roomNo);
        params.put("checkin", checkin);
        params.put("checkout", checkout);
        
        int conflict = reservationMapper.checkRoomAvailability(params);
        return conflict == 0; // 0이면 예약 가능
    }
    
    @Override
    @Transactional
    public boolean createReservation(ReservationDto reservation) {
        try {
            // 1. 체크아웃 날짜 자동 계산
            reservation.calculateCheckout();
            
            // 2. 예약 가능 여부 확인
            boolean available = isRoomAvailable(
                reservation.getRoomNo(), 
                reservation.getCheckin(), 
                reservation.getCheckout()
            );
            
            if (!available) {
                throw new RuntimeException("선택하신 기간에 이미 예약이 있습니다.");
            }
            
            // 3. status 기본값 설정
            if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
                reservation.setStatus("예약중");
            }
            
            // 4. 예약 생성
            int result = reservationMapper.insertReservation(reservation);
            
            // 5. 서비스 등록
            if (reservation.getServiceIds() != null && !reservation.getServiceIds().isEmpty()) {
                Long resNo = reservation.getResNo();
                for (Long serviceNo : reservation.getServiceIds()) {
                    reservationMapper.insertReservationService(resNo, serviceNo);
                }
            }
            
            return result > 0;
            
        } catch (Exception e) {
            throw new RuntimeException("예약 생성 실패: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional
    public boolean cancelReservation(Long resNo) {
        int result = reservationMapper.cancelReservation(resNo);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean completeReservation(Long resNo) {
        int result = reservationMapper.completeReservation(resNo);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean updateReservationStatus(Long resNo, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("resNo", resNo);
        params.put("status", status);
        
        int result = reservationMapper.updateReservationStatus(params);
        return result > 0;
    }
    
    @Override
    public List<ReservationDto> getTodayCheckIns() {
        List<ReservationDto> reservations = reservationMapper.getTodayCheckIns();
        
        // nights 계산
        for (ReservationDto res : reservations) {
            if (res.getCheckin() != null && res.getCheckout() != null) {
                long days = ChronoUnit.DAYS.between(res.getCheckin(), res.getCheckout());
                res.setNights((int) days);
            }
        }
        
        return reservations;
    }
    
    @Override
    public List<ReservationDto> getTodayCheckOuts() {
        List<ReservationDto> reservations = reservationMapper.getTodayCheckOuts();
        
        // nights 계산
        for (ReservationDto res : reservations) {
            if (res.getCheckin() != null && res.getCheckout() != null) {
                long days = ChronoUnit.DAYS.between(res.getCheckin(), res.getCheckout());
                res.setNights((int) days);
            }
        }
        
        return reservations;
    }
    
    @Override
    public List<ReservationDto> getRoomSchedule(Long roomNo) {
        List<ReservationDto> reservations = reservationMapper.getRoomSchedule(roomNo);
        
        // nights 계산
        for (ReservationDto res : reservations) {
            if (res.getCheckin() != null && res.getCheckout() != null) {
                long days = ChronoUnit.DAYS.between(res.getCheckin(), res.getCheckout());
                res.setNights((int) days);
            }
        }
        
        return reservations;
    }

    /* 예약 수정  */
    @Override
    @Transactional
    public boolean updateReservation(ReservationDto reservation) {

        // 🔹 1. 자기 자신(resNo)은 제외하고 날짜 겹침 검사
        Map<String, Object> params = new HashMap<>();
        params.put("roomNo", reservation.getRoomNo());
        params.put("checkin", reservation.getCheckin());
        params.put("checkout", reservation.getCheckout());
        params.put("resNo", reservation.getResNo());   // ⭐ 본인 예약 제외

        int conflict = reservationMapper.checkRoomAvailabilityForUpdate(params);

        if (conflict > 0) {
            return false;   // ❌ 겹치는 예약 있음 → 수정 불가
        }

        // 🔹 2. 예약 날짜 + 금액 수정
        int cnt = reservationMapper.update(
            reservation.getResNo(),
            reservation.getCheckin(),
            reservation.getCheckout(),
            reservation.getTotal(),
            reservation.getTotalPrice()
        );

        if (cnt != 1) {
            throw new RuntimeException("예약 수정 실패: resNo=" + reservation.getResNo());
        }

        // 🔹 3. 서비스 갱신
        reservationMapper.deleteReservationServices(reservation.getResNo());

        if (reservation.getServiceIds() != null && !reservation.getServiceIds().isEmpty()) {
            for (Long serviceNo : reservation.getServiceIds()) {
                reservationMapper.insertReservationService(reservation.getResNo(), serviceNo);
            }
        }

        return true;
    }

    /**
     * ⭐ 활성화된 예약 조회 (CCTV용)
     */
    @Override
    public ReservationDto getActiveReservation(Long userNo, LocalDate today) {
        try {
            ReservationDto reservation = reservationMapper.selectActiveReservation(userNo, today);
            
            // nights 계산
            if (reservation != null && reservation.getCheckin() != null && reservation.getCheckout() != null) {
                long days = ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout());
                reservation.setNights((int) days);
            }
            
            return reservation;
        } catch (Exception e) {
            log.error("활성 예약 조회 중 오류 발생", e);
            return null;
        }
    }

    @Override
    public List<userTotalSales> getMemberTotalSales() {
        return reservationMapper.getMemberTotalSales();
    }

    @Override
    public List<MonthlySalesDto> getMonthlySales() {
       return reservationMapper.getMonthlySales();
    }
}
