package com.aloha.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HotelRoom {

    private Long roomNo;        // 호텔 객실 번호 (PK)
    private String roomType;    // 객실 호수 (101호, 102호)
    private int roomPrice;      // 가격
    private String etc;         // 세부 사항
    private String active;      // 예약 여부
    private String img;         // 이미지 파일명
}
