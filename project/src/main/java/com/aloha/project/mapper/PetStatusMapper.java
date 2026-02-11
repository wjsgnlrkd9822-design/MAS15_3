package com.aloha.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.project.dto.PetStatus;

@Mapper
public interface PetStatusMapper {
    
    /**
     * 현재 체크인 중인 반려견 목록 + 상태 조회 (관리자용)
     * @return 반려견 정보 + 예약 정보 + 상태 정보
     */
    List<Map<String, Object>> selectActiveReservationsWithPetStatus();
    
    /**
     * 반려견 상태 조회
     * @param petNo 반려견 번호
     * @return 반려견 상태 정보
     */
    PetStatus selectPetStatus(@Param("petNo") Long petNo);
    
    /**
     * 반려견 상태 등록
     * @param petStatus 반려견 상태 정보
     * @return 등록된 행 수
     */
    int insertPetStatus(PetStatus petStatus);
    
    /**
     * 반려견 상태 수정
     * @param petStatus 반려견 상태 정보
     * @return 수정된 행 수
     */
    int updatePetStatus(PetStatus petStatus);
    
    /**
     * 반려견 상태 삭제
     * @param petNo 반려견 번호
     * @return 삭제된 행 수
     */
    int deletePetStatus(@Param("petNo") Long petNo);
}
