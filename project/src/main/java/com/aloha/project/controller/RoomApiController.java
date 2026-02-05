package com.aloha.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.project.dto.HotelRoom;
import com.aloha.project.service.HotelRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoomApiController {

     private final HotelRoomService hotelRoomService;

    @GetMapping("/api/room/{roomNo}")
    public HotelRoom getRoom(@PathVariable("roomNo") Long roomNo) {
        return hotelRoomService.getRoom(roomNo);
    }
    
}
