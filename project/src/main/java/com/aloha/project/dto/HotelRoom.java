package com.aloha.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HotelRoom {

    private Long roomNo;        // 호텔 객실 번호
    private String roomType;    // 객실 호수
    private int roomPrice;      // 가격
    private String etc;         // 세부 사항
    private String active;      // 예약 여부
    private String img;         // 이미지 파일명

    // ✅ 기본 생성자
    public HotelRoom() {
    }

    // ✅ roomNo 제외하고 바로 값 넣을 수 있는 생성자
    public HotelRoom(String roomType, int roomPrice, String etc, String active, String img) {
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.etc = etc;
        this.active = active;
        this.img = img;
    }
}
