package com.aloha.project.dto;

import lombok.Data;

@Data
public class KakaoPayApproveResponse {
    private String aid;                 // 요청 고유 번호
    private String tid;                 // 거래 고유 번호
    private String cid;                 // 가맹점 코드
    private String partner_order_id;    // 가맹점 주문 번호
    private String partner_user_id;     // 가맹점 회원 ID
    private String payment_method_type; // 결제 수단 (CARD / MONEY)
    private Amount amount;              // 결제 금액 정보
    private String item_name;           // 상품명
    private int quantity;               // 상품 수량
    private String created_at;          // 결제 준비 시각
    private String approved_at;         // 결제 승인 시각

    @Data
    public static class Amount {
        private int total;              // 전체 결제 금액
        private int tax_free;           // 비과세 금액
        private int vat;                // 부가세 금액
    }
}