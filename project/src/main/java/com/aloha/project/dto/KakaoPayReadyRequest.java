package com.aloha.project.dto;

import lombok.Data;

@Data
public class KakaoPayReadyRequest {
    private String cid;                 // 가맹점 코드 (테스트: TC0ONETIME)
    private String partner_order_id;    // 가맹점 주문 번호 (res_no)
    private String partner_user_id;     // 가맹점 회원 ID (user_no)
    private String item_name;           // 상품명
    private int quantity;               // 상품 수량
    private int total_amount;           // 총 금액 (total_price)
    private int tax_free_amount;        // 비과세 금액
    private String approval_url;        // 결제 성공 URL
    private String fail_url;            // 결제 실패 URL
    private String cancel_url;          // 결제 취소 URL
}