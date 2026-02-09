package com.aloha.project.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.project.dto.HotelService;
import com.aloha.project.dto.ReservationDto;

@Mapper
public interface ReservationMapper {

    int insertReservation(ReservationDto dto);

    /* int insert(
        @Param("userNo") Long userNo,
        @Param("petNo") Long petNo,
        @Param("roomNo") Long roomNo,
        @Param("checkinDate") LocalDate checkinDate,
        @Param("checkoutDate") LocalDate checkoutDate,
        @Param("resTime") LocalTime resTime,
        @Param("totalPrice") int totalPrice  
    ); */

    List<ReservationDto> findByUserNo(@Param("userNo") Long userNo);
    
    // ✅ 추가
    ReservationDto findByResNo(@Param("resNo") Long resNo);
    
    // ✅ 추가
    int update(
        @Param("resNo") Long resNo,
        @Param("checkinDate") LocalDate checkinDate,
        @Param("checkoutDate") LocalDate checkoutDate,
        @Param("total") int total,
        @Param("totalPrice") int totalPrice
    );

   void deleteReservation(Long resNo); 

    void insertReservationService(@Param("resNo") Long resNo, @Param("serviceNo") Long serviceNo);

    void deleteReservationServices(@Param("resNo") Long resNo);

    int getServicePrice(@Param("serviceNo") Long serviceNo);

     List<HotelService> selectServicesByReservation(@Param("resNo") Long resNo);
    
    // ✅ 예약의 선택된 서비스 번호 조회
    List<Long> selectServiceIdsByReservation(@Param("resNo") Long resNo);
    
}