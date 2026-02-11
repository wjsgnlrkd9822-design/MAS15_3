package com.aloha.project.dto;

import lombok.Data;

@Data
public class KakaoPayReadyResponse {
    private String tid;  
    private String next_redirect_pc_url;
    private String next_redirect_mobile_url;
    private String next_redirect_app_url;  
    private String created_at;           
}