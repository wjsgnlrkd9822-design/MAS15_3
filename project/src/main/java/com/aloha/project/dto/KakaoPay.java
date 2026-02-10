package com.aloha.project.dto;

import lombok.Data;

@Data
public class KakaoPay {
    private String tid;                     // 거래 ID
    private String next_redirect_pc_url;    // 결제 페이지 URL
    private String created_at;
}