package com.aloha.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.HotelRoom;

@Mapper
public interface RoomMapper {
    
  List<HotelRoom> list() throws Exception;

  int insert(HotelRoom hotelRoom) throws Exception;

  int update(HotelRoom hotelRoom) throws Exception;
  
  HotelRoom select(Long roomNo) throws Exception;

  int delete(Long roomNo) throws Exception;
}
