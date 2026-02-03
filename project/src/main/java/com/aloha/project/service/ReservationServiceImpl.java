package com.aloha.project.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.project.dto.ReservationDto;
import com.aloha.project.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;

    @Override
    @Transactional
    public void insert(Long userNo, Long petNo, Long roomNo, LocalDate checkinDate, LocalDate checkoutDate, LocalTime resTime) {
        int cnt = reservationMapper.insert(userNo, petNo, roomNo, checkinDate, checkoutDate, resTime);
        if (cnt != 1) {
            throw new RuntimeException("예약 추가 실패: userNo=" + userNo + ", petNo=" + petNo + ", roomNo=" + roomNo);
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

    // ✅ 추가
    @Override
    public ReservationDto getReservationByResNo(Long resNo) {
        ReservationDto reservation = reservationMapper.findByResNo(resNo);
        if (reservation != null && reservation.getCheckin() != null && reservation.getCheckout() != null) {
            long days = ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout());
            reservation.setNights((int) days);
        }
        return reservation;
    }

    // ✅ 추가
    @Override
    @Transactional
    public void update(Long resNo, LocalDate checkinDate, LocalDate checkoutDate, int total) {
        int cnt = reservationMapper.update(resNo, checkinDate, checkoutDate, total);
        if (cnt != 1) {
            throw new RuntimeException("예약 수정 실패: resNo=" + resNo);
        }
    }
}