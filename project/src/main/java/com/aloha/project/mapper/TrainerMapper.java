package com.aloha.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.Trainer;

@Mapper
public interface TrainerMapper {
    

    public List<Trainer> list() throws Exception;
    public Trainer select(Long trainerNo) throws Exception;
    public int insert(Trainer trainer) throws Exception;       
    public int update(Trainer trainer) throws Exception;   
    public int delete(Long trainerNo) throws Exception;
}
