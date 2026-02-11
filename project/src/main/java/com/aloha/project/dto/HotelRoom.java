package com.aloha.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HotelRoom {

    private Long roomNo;        // 호텔 객실 번호
    private String roomType;    // 객실 종류
    private int roomPrice;      // 가격
    private String etc;         // 세부 사항
    private String active;      // 예약 여부
    private String img;         // 이미지 파일명
    private String cctvUrl;     // ⭐ CCTV 유튜브 라이브 URL

    public HotelRoom() {}
}
