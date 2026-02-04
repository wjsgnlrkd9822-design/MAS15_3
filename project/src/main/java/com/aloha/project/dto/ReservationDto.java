package com.aloha.project.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservationDto {
    private Long resNo;
    private Long userNo;
    private Long petNo;
    private Long rommNo;
    private LocalDate checkin;
    private LocalDate checkout; // calculateCheckout에서 계산
    private int nights;        
    private int total;          
    private String petName;
    private String roomType;

    public void calculateCheckout() {
        if (checkin != null) {
            this.checkout = checkin.plusDays(nights);
        }
    }
}

