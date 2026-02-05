package com.aloha.project.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ReservationDto {
    private Long resNo;
    private Long petNo;
    private LocalDate checkin;
    private LocalDate checkout; // calculateCheckout에서 계산
    private int nights;        
    private int total;   
    private int totalPrice;       
    private String petName;
    private String roomType;
    private Long roomNo;
    private Long userNo;
    private String resTime;

    private List<Long> serviceIds;
    private List<HotelService> services;

    public void calculateCheckout() {
        if (checkin != null) {
            this.checkout = checkin.plusDays(nights);
        }
    }

    
}

