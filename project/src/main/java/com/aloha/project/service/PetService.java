package com.aloha.project.service;


import com.aloha.project.dto.Pet;

public interface PetService {

    // 조회
    public Pet select(Long no) throws Exception;

    // 등록
    public int insert(Pet pet) throws Exception;

    // 수정
    public int update(Pet pet) throws Exception;

    // 삭제
    public int delete(Long petNo) throws Exception;
    
}
