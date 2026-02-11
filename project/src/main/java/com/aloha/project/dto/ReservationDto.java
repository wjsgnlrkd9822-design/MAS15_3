package com.aloha.project.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ReservationDto {
    private Long resNo;
    private Long petNo; 
    private LocalDate checkin;
    private LocalDate checkout;
    private int nights;        
    private int total;   
    private int totalPrice;       
    private String petName;
    private String roomType;
    private Long roomNo;
    private Long userNo;
    private String resTime;
    private String status;          /* 예약 상태 */
    private String cctvUrl;         // ⭐ CCTV URL 추가
    private String roomImg;         // ⭐ 객실 이미지 추가
    private PetStatus petStatus;    // ⭐ 반려견 상태 정보 추가

    private List<Long> serviceIds;
    private List<HotelService> services;

    public void calculateCheckout() {
        if (checkin != null) {
            this.checkout = checkin.plusDays(nights);
        }
    }
}
