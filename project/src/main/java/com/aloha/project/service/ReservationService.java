package com.aloha.project.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.aloha.project.dto.HotelRoom;
import com.aloha.project.dto.HotelService;
import com.aloha.project.dto.ReservationDto;

public interface ReservationService {

    // ✅ 기존 메서드
    void insert(Long userNo, Long petNo, Long roomNo, LocalDate checkinDate, LocalDate checkoutDate, 
                LocalTime resTime, int totalPrice, List<Long> serviceNos);

    List<ReservationDto> getReservationsByUser(Long userNo);
    
    ReservationDto getReservationByResNo(Long resNo);
    
    void update(Long resNo, LocalDate checkinDate, LocalDate checkoutDate, int total, int totalPrice, List<Long> serviceNos);

    
    void delete(Long resNo);

    Long getTotalSales();

    int getServicePrice(Long serviceNo);

    List<HotelService> getServicesByReservation(Long resNo);

    // ⭐ 새로 추가: 날짜별 예약 관리 메서드
    
    /**
     * 예약 가능한 객실 조회
     * @param roomType 객실 타입 (예: "Large Dog", "Medium Dog")
     * @param checkin 체크인 날짜
     * @param checkout 체크아웃 날짜
     * @return 예약 가능한 객실 리스트
     */
    List<HotelRoom> getAvailableRooms(String roomType, LocalDate checkin, LocalDate checkout);
    
    /**
     * 예약 가능 여부 확인
     * @param roomNo 객실 번호
     * @param checkin 체크인 날짜
     * @param checkout 체크아웃 날짜
     * @return true: 예약 가능, false: 예약 불가
     */
    boolean isRoomAvailable(Long roomNo, LocalDate checkin, LocalDate checkout);
    
    /**
     * 예약 생성 (날짜 겹침 체크 포함)
     * @param reservation 예약 정보
     * @return 성공 여부
     */
    boolean createReservation(ReservationDto reservation);
    
    /**
     * 예약 취소
     * @param resNo 예약 번호
     * @return 성공 여부
     */
    boolean cancelReservation(Long resNo);
    
    /**
     * 체크아웃 처리
     * @param resNo 예약 번호
     * @return 성공 여부
     */
    boolean completeReservation(Long resNo);
    
    /**
     * 예약 상태 변경
     * @param resNo 예약 번호
     * @param status 변경할 상태 ("예약중", "취소", "완료")
     * @return 성공 여부
     */
    boolean updateReservationStatus(Long resNo, String status);
    
    /**
     * 오늘 체크인 목록 조회
     * @return 오늘 체크인 예정 리스트
     */
    List<ReservationDto> getTodayCheckIns();
    
    /**
     * 오늘 체크아웃 목록 조회
     * @return 오늘 체크아웃 예정 리스트
     */
    List<ReservationDto> getTodayCheckOuts();
    
    /**
     * 객실별 예약 스케줄 조회
     * @param roomNo 객실 번호
     * @return 예약 스케줄 리스트
     */
    List<ReservationDto> getRoomSchedule(Long roomNo);

        /**
     * 예약 수정 (날짜 겹침 체크 포함)
     * @param reservation 예약 정보
     * @return true: 수정 성공, false: 날짜 겹침
     */
    boolean updateReservation(ReservationDto reservation);

    }

