package com.aloha.project.service;

import com.aloha.project.dto.KakaoPayApproveResponse;
import com.aloha.project.dto.KakaoPayCancelResponse;
import com.aloha.project.dto.KakaoPayReadyResponse;
import jakarta.servlet.http.HttpSession;

public interface KakaoPayService {

    // 결제 준비 (Ready)
    KakaoPayReadyResponse ready(Long resNo, int totalPrice, HttpSession session);

    // 결제 승인 (Approve)
    KakaoPayApproveResponse approve(String pgToken, HttpSession session);

    // 결제 취소 (Cancel)
    KakaoPayCancelResponse cancel(Long resNo, int cancelAmount);



}