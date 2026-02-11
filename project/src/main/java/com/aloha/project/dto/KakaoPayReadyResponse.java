package com.aloha.project.dto;

import lombok.Data;

@Data
public class KakaoPayReadyResponse {
    private String tid;                         // 거래 고유 번호
    private String next_redirect_pc_url;        // PC 결제 페이지 URL
    private String next_redirect_mobile_url;    // 모바일 결제 페이지 URL
    private String next_redirect_app_url;       // 앱 결제 페이지 URL
    private String created_at;                  // 결제 준비 시각
}