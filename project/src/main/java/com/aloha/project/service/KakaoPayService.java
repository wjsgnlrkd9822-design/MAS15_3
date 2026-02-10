package com.aloha.project.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.aloha.project.dto.KakaoPay;

@Service
public class KakaoPayService {

    private static final String HOST = "https://kapi.kakao.com";
    private static final String ADMIN_KEY = "KAKAO_ADMIN_KEY_DEV";

    public KakaoPay readyPay(Long resNo, int totalPrice) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + ADMIN_KEY);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");  // 테스트용
        params.add("partner_order_id", resNo.toString());
        params.add("partner_user_id", "user123"); // 실제 로그인 userId
        params.add("item_name", "반려동물 호텔 예약");
        params.add("quantity", "1");
        params.add("total_amount", String.valueOf(totalPrice));
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:8080/pay/success");
        params.add("cancel_url", "http://localhost:8080/pay/cancel");
        params.add("fail_url", "http://localhost:8080/pay/fail");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<KakaoPay> response = restTemplate.postForEntity(
                HOST + "/v1/payment/ready", request, KakaoPay.class);

        return response.getBody();
    }
}