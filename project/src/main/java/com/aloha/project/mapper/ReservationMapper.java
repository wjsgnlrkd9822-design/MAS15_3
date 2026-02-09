package com.aloha.project.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.project.dto.HotelRoom;
import com.aloha.project.dto.HotelService;
import com.aloha.project.dto.ReservationDto;

@Mapper
public interface ReservationMapper {

    // ✅ 기존 메서드
    int insertReservation(ReservationDto dto);

    List<ReservationDto> findByUserNo(@Param("userNo") Long userNo);
    
    ReservationDto findByResNo(@Param("resNo") Long resNo);
    
    int update(
        @Param("resNo") Long resNo,
        @Param("checkinDate") LocalDate checkinDate,
        @Param("checkoutDate") LocalDate checkoutDate,
        @Param("total") int total,
        @Param("totalPrice") int totalPrice
    );
    
    Long getTotalSales();

    void deleteReservation(Long resNo); 

    void insertReservationService(@Param("resNo") Long resNo, @Param("serviceNo") Long serviceNo);

    void deleteReservationServices(@Param("resNo") Long resNo);

    int getServicePrice(@Param("serviceNo") Long serviceNo);

    List<HotelService> selectServicesByReservation(@Param("resNo") Long resNo);
    
    List<Long> selectServiceIdsByReservation(@Param("resNo") Long resNo);
    
    // ⭐ 새로 추가: 날짜별 예약 관리 메서드
    
    /**
     * 예약 가능한 객실 조회 (날짜별)
     * @param params - roomType, checkin, checkout 포함
     * @return 예약 가능한 객실 리스트
     */
    List<HotelRoom> getAvailableRooms(Map<String, Object> params);
    
    /**
     * 예약 가능 여부 확인 (날짜 겹침 체크)
     * @param params - roomNo, checkin, checkout 포함
     * @return 겹치는 예약 수 (0이면 예약 가능)
     */
    int checkRoomAvailability(Map<String, Object> params);
    
    /**
     * 예약 취소 (status를 '취소'로 변경)
     * @param resNo 예약 번호
     * @return 업데이트된 행 수
     */
    int cancelReservation(@Param("resNo") Long resNo);
    
    /**
     * 체크아웃 처리 (status를 '완료'로 변경)
     * @param resNo 예약 번호
     * @return 업데이트된 행 수
     */
    int completeReservation(@Param("resNo") Long resNo);
    
    /**
     * 예약 상태 업데이트
     * @param params - resNo, status 포함
     * @return 업데이트된 행 수
     */
    int updateReservationStatus(Map<String, Object> params);
    
    /**
     * 오늘 체크인 목록 조회
     * @return 오늘 체크인 예정 예약 리스트
     */
    List<ReservationDto> getTodayCheckIns();
    
    /**
     * 오늘 체크아웃 목록 조회
     * @return 오늘 체크아웃 예정 예약 리스트
     */
    List<ReservationDto> getTodayCheckOuts();
    
    /**
     * 객실별 예약 스케줄 조회
     * @param roomNo 객실 번호
     * @return 해당 객실의 예약 스케줄 리스트
     */
    List<ReservationDto> getRoomSchedule(@Param("roomNo") Long roomNo);
    
    int checkRoomAvailabilityForUpdate(Map<String, Object> params);

    
}