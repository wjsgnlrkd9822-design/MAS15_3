package com.aloha.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.HotelRoom;

@Mapper
public interface RoomMapper {
    
  public List<HotelRoom> list() throws Exception;
 
  public int insert(HotelRoom hotelRoom) throws Exception;

  public int update(HotelRoom hotelRoom) throws Exception;
  
  public HotelRoom select(Long roomNo) throws Exception;

  public int delete(Long roomNo) throws Exception;
}
