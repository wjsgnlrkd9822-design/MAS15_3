package com.aloha.project.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetStatus {
    
    private Long petNo;           // 반려견 번호
    private String status;        // 상태 (RESTING, PLAYING, EATING, WALKING)
    private String nextSchedule;  // 다음 일정 (예: "16:00 산책")
    private Timestamp updatedAt;  // 마지막 업데이트 시간
    
    // 상태를 한글로 변환하는 메서드
    public String getStatusKorean() {
        if (status == null) return "알 수 없음";
        
        switch (status.toUpperCase()) {
            case "RESTING":
                return "휴식중";
            case "PLAYING":
                return "놀이중";
            case "EATING":
                return "식사중";
            case "WALKING":
                return "산책중";
            default:
                return status;
        }
    }
}
