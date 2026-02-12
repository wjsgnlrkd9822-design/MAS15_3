package com.aloha.project.controller;

import com.aloha.project.dto.KakaoPayApproveResponse;
import com.aloha.project.dto.KakaoPayCancelResponse;
import com.aloha.project.dto.KakaoPayReadyResponse;
import com.aloha.project.service.KakaoPayService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/kakaopay")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    // 결제 준비 (마이페이지 결제 버튼 클릭 시)
    @PostMapping("/ready")
public String ready(@RequestParam("resNo") Long resNo,
                    @RequestParam("totalPrice") int totalPrice,
                    HttpSession session) {

    KakaoPayReadyResponse response = kakaoPayService.ready(resNo, totalPrice, session);
    return "redirect:" + response.getNext_redirect_pc_url();
}

    // 결제 성공
    @GetMapping("/success")
    public String success(@RequestParam("pg_token") String pgToken,
                          HttpSession session,
                          Model model) {

        KakaoPayApproveResponse response = kakaoPayService.approve(pgToken, session);
        model.addAttribute("approve", response);

        log.info("결제 완료 - 상품명: {}, 금액: {}", response.getItem_name(), response.getAmount().getTotal());

        return "kakaopay/success";
    }

    // 결제 실패
    @GetMapping("/fail")
    public String fail() {
        log.warn("결제 실패");
        return "kakaopay/fail";
    }

    // 결제 취소
    @GetMapping("/cancel")
    public String cancel() {
        log.warn("결제 취소");
        return "kakaopay/cancel";
    }

    // 환불
    @PostMapping("/refund")
    public String refund(@RequestParam("resNo") Long resNo,
                        @RequestParam("cancelAmount") int cancelAmount,
                        Model model) {

        KakaoPayCancelResponse response = kakaoPayService.cancel(resNo, cancelAmount);
        model.addAttribute("cancel", response);

        log.info("환불 완료 - resNo: {}, 금액: {}", resNo, cancelAmount);

        return "kakaopay/refund";
    }

}