package com.aloha.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.Pet;

@Mapper
public interface PetMapper {

    // 반려동물 조회
    public Pet select(Long no) throws Exception;

    // 반려동물 등록
    public int insert(Pet pet) throws Exception;

    // 반려동물 수정
    public int update(Pet pet) throws Exception;

    // 반려동물 삭제
    public int delete(Long no) throws Exception;

    // ownerNo 기준 리스트 조회
    public List<Pet> selectPetsByOwnerNo(Long ownerNo) throws Exception;
    
}
