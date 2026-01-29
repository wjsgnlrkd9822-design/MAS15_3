package com.aloha.project.mapper;

import java.util.List;

import com.aloha.project.dto.HotelRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelRoomMapper {
    int insertHotelRoom(HotelRoom room);
    HotelRoom selectHotelRoomById(Long roomNo);
    List<HotelRoom> selectAllHotelRooms();
    int updateHotelRoom(HotelRoom room);
    int deleteHotelRoom(Long roomNo);
}
