package com.aloha.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HotelService {

    private Long serviceNo;       // 서비스 번호
    private String serviceName;   // 서비스 명
    private String description;   // 서비스 설명
    private int servicePrice;     // 서비스 가격

    public HotelService() {}
}
