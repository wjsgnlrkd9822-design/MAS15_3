package com.aloha.project.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.aloha.project.dto.ReservationDto;

public interface ReservationService {

    void insert(Long userNo, Long petNo, Long roomNo, LocalDate checkinDate, LocalDate checkoutDate, LocalTime resTime);

    List<ReservationDto> getReservationsByUser(Long userNo);
    
    // ✅ 추가
    ReservationDto getReservationByResNo(Long resNo);
    
    // ✅ 추가
    void update(Long resNo, LocalDate checkinDate, LocalDate checkoutDate, int total);
    
    void delete(Long resNo);

}