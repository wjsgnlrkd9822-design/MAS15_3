package com.aloha.project.service;


import com.aloha.project.dto.Pet;
import java.util.List;

public interface PetService {

    // 조회
    public Pet select(Long no) throws Exception;

    // 등록
    public int insert(Pet pet) throws Exception;

    // 수정
    public int update(Pet pet) throws Exception;

    // 삭제
    public int delete(Long petNo) throws Exception;
    
    // ownerNo로 반려견 목록 조회
    public List<Pet> selectPetsByOwnerNo(Long ownerNo) throws Exception;

    // 반려견 이미지 조회
    Pet selectPetImage(Long petNo) throws Exception;
    
}
