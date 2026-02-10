package com.aloha.project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.aloha.project.dto.CustomUser;
import com.aloha.project.dto.KakaoPay;

public class KakaoPayController {

    @PostMapping("/{resNo}")
public Map<String, Object> pay(@PathVariable Long resNo,
                               @AuthenticationPrincipal CustomUser customUser) throws Exception {

    // 로그인 사용자 UUID 가져오기
    String partnerUserId = customUser.getUser().getId();

    // DB에서 totalPrice 가져오기
    int totalPrice = reservationMapper.getTotalPrice(resNo);

    // 카카오페이 결제 준비
    KakaoPay kakaoPay = KakaoPayService.readyPay(resNo, totalPrice, partnerUserId);

    Map<String, Object> result = new HashMap<>();
    result.put("next_redirect_pc_url", kakaoPay.getNext_redirect_pc_url());
    result.put("tid", kakaoPay.getTid());

    return result;
}
    
}
