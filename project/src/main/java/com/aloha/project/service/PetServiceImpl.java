package com.aloha.project.service;

import org.springframework.stereotype.Service;


import com.aloha.project.dto.Pet;
import com.aloha.project.mapper.PetMapper;
import com.aloha.project.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PetServiceImpl implements PetService {

    private final PetMapper petMapper;
    /* private final UserMapper userMapper; */

    public PetServiceImpl(PetMapper petMapper, UserMapper userMapper) {
        this.petMapper = petMapper;
        /* this.userMapper = userMapper; */
    }

    // 단일 조회
    @Override
    public Pet select(Long no) throws Exception {
        return petMapper.select(no);
    }

    // 등록
    @Override
    public int insert(Pet pet) throws Exception {
        if (pet.getOwnerNo() == null ) {
            throw new IllegalArgumentException("ownerNo가 null입니다. 로그인 UUID 확인 필요");
        }
        return petMapper.insert(pet);
    }

    // 수정
    @Override
    public int update(Pet pet) throws Exception {
        return petMapper.update(pet);
    }

    // 삭제
    @Override
    public int delete(Long no) throws Exception {
        return petMapper.delete(no);
    }

    // ownerNo로 반려견 목록 조회
    @Override
    public List<Pet> selectPetsByOwnerNo(Long ownerNo) throws Exception {
        return petMapper.selectPetsByOwnerNo(ownerNo);
    }

    @Override
    public Pet selectPetImage(Long petNo) throws Exception {
        return petMapper.selectPetImage(petNo);

    }

}