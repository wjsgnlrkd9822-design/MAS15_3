package com.aloha.project.mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.project.dto.ReservationDto;

@Mapper
public interface ReservationMapper {

    int insert(
        @Param("userNo") Long userNo,
        @Param("petNo") Long petNo,
        @Param("roomNo") Long roomNo,
        @Param("checkinDate") LocalDate checkinDate,
        @Param("checkoutDate") LocalDate checkoutDate,
        @Param("resTime") LocalTime resTime,
        @Param("totalPrice") int totalPrice  
    );

    List<ReservationDto> findByUserNo(@Param("userNo") Long userNo);
    
    ReservationDto findByResNo(@Param("resNo") Long resNo);
    
    int update(
        @Param("resNo") Long resNo,
        @Param("checkinDate") LocalDate checkinDate,
        @Param("checkoutDate") LocalDate checkoutDate,
        @Param("total") int total,
        @Param("totalPrice") int totalPrice
    );

    int deleteReservation(@Param("resNo") Long resNo); // ✅ void → int, @Param 추가
    
}