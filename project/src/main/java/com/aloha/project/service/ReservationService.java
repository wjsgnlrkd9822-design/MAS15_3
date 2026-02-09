package com.aloha.project.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.aloha.project.dto.HotelService;
import com.aloha.project.dto.ReservationDto;

public interface ReservationService {

    void insert(Long userNo, Long petNo, Long roomNo, LocalDate checkinDate, LocalDate checkoutDate, 
                LocalTime resTime, int totalPrice, List<Long> serviceNos);

    List<ReservationDto> getReservationsByUser(Long userNo);
    
    // ✅ 추가
    ReservationDto getReservationByResNo(Long resNo);
    
    // ✅ 추가
    void update(Long resNo, LocalDate checkinDate, LocalDate checkoutDate, int total, int totalPrice, List<Long> serviceNos);
    
    void delete(Long resNo);

    public Long getTotalSales();


    // 추가: 서비스 가격 조회
    int getServicePrice(Long serviceNo);

    // 추가: 예약에 선택된 서비스 조회
    List<HotelService> getServicesByReservation(Long resNo);


    

}

