package com.aloha.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.Trainer;

@Mapper
public interface TrainerMapper {
    

    List<Trainer> list() throws Exception;
    Trainer select(Long trainerNo) throws Exception;
    int insert(Trainer trainer) throws Exception;       
    int update(Trainer trainer) throws Exception;   
    int delete(Long trainerNo) throws Exception;
}
