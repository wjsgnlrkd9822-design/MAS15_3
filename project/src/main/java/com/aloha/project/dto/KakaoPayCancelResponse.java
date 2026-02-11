package com.aloha.project.dto;

import lombok.Data;

@Data
public class KakaoPayCancelResponse {
    private String aid;                  // 요청 고유 번호
    private String tid;                  // 거래 고유 번호
    private String cid;                  // 가맹점 코드
    private String status;               // 결제 상태
    private String partner_order_id;     // 가맹점 주문 번호
    private String partner_user_id;      // 가맹점 회원 ID
    private String payment_method_type;  // 결제 수단
    private CancelAmount amount;         // 취소 금액 정보
    private CancelAmount approved_cancel_amount;  // 이번 요청으로 취소된 금액
    private String item_name;            // 상품명
    private String canceled_at;          // 취소 시각

    @Data
    public static class CancelAmount {
        private int total;       // 전체 취소 금액
        private int tax_free;    // 비과세 금액
        private int vat;         // 부가세 금액
    }
}