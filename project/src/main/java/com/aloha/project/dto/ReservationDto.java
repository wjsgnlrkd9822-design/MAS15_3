package com.aloha.project.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservationDto {
    private Long resNo;
    private Long userNo;
    private Long petNo;
    private Long roomNo;  // ✅ 추가
    private LocalDate checkin;
    private LocalDate checkout;
    private int nights;        
    private int total;   
    private int totalPrice;       
    private String petName;
    private String roomType;

    public void calculateCheckout() {
        if (checkin != null) {
            this.checkout = checkin.plusDays(nights);
        }
    }
}