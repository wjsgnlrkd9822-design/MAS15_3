package com.aloha.project.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.project.dto.HotelService;
import com.aloha.project.dto.ReservationDto;
import com.aloha.project.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;

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
        dto.setPetNo(petNo);  // ✅ 추가
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
        Long resNo = dto.getResNo(); // ✅ 여기서 정확한 방금 insert된 예약 ID 확보

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
            // ✅ checkout은 이미 DB에서 가져왔으니 nights만 계산
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
            // 예약 일자 계산
            if (reservation.getCheckin() != null && reservation.getCheckout() != null) {
                long days = ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout());
                reservation.setNights((int) days);
            }
            
            // ✅ 서비스 ID 조회 및 설정
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
            return (total == null) ? 0L : total; //
    }
}