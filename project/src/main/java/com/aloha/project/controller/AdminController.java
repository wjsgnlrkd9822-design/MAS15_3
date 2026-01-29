package com.aloha.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.project.dto.HotelRoom;
import com.aloha.project.dto.HotelService;
import com.aloha.project.service.AddtionalService;
import com.aloha.project.service.FileService;
import com.aloha.project.service.RoomService;

import lombok.RequiredArgsConstructor;





@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
  
  private final AddtionalService addtionalService;
  private final RoomService roomService;
  private final FileService fileService;
  @GetMapping("")
  public String admin() {
    return "admin/ad_main";
  }
  

  @GetMapping("/service")
public String service(Model model) {
    try {
        List<HotelRoom> roomList = roomService.list(); // DB 조회
        model.addAttribute("roomList", roomList);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "admin/ad_service";
}


@GetMapping("/roomup")
public String roomUpdate(@RequestParam("roomNo") Long roomNo, Model model) {
    try {
        HotelRoom room = roomService.select(roomNo);
        System.out.println("DB 조회 결과: " + room); // 확인용 로그
        model.addAttribute("room", room);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "admin/ad_roomupdate";
}

@PreAuthorize("hasRole('ADMIN')")
@PostMapping("/upload")
@ResponseBody
public ResponseEntity<String> uploadImage(@RequestParam("imgFile") MultipartFile imgFile) {
    try {
        if (imgFile == null || imgFile.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        // 파일 저장
        String savedFileName = fileService.save(imgFile);

        return ResponseEntity.ok(savedFileName); // JS로 저장된 파일명 반환
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL");
    }
}
@PreAuthorize("hasRole('ADMIN')")
@PostMapping("/update/{roomNo}")
@ResponseBody
public ResponseEntity<?> updateRoom(
        @PathVariable Long roomNo,
        @RequestBody HotelRoom hotelRoom // JSON 바인딩
) {
    try {
        // roomNo 세팅
        hotelRoom.setRoomNo(roomNo);

        // DB 수정
        boolean result = roomService.update(hotelRoom);

        if (!result) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);

    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


@GetMapping("/roominsert")
public String roominsert() {
    return "admin/ad_roominsert";
}

@PreAuthorize("hasRole('ADMIN')")
@PostMapping("/add")
@ResponseBody
public ResponseEntity<?> createRoom(@RequestBody HotelRoom hotelRoom) {
    try {
        // 1️⃣ DB 저장
        boolean result = roomService.insert(hotelRoom);

        if (!result) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/delete/{roomNo}") 
public ResponseEntity<?> delete(@PathVariable("roomNo") Long roomNo) {

    try {
        boolean result = roomService.delete(roomNo);
        if (!result) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/serviceinsert")
public String serviceinsert() {
    return "admin/ad_serviceinsert";
}

@PreAuthorize("hasRole('ADMIN')")
@PostMapping("/serviceadd")
@ResponseBody
public ResponseEntity<String> createService(@RequestBody HotelService hotelService) {
    try {
        boolean result = addtionalService.insert(hotelService);

        if (!result) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}









  @GetMapping("/notice")
  public String notice() {
      return "admin/ad_notice";
  }

  
  @GetMapping("/trainer") 
   public String trainer() {
      return "admin/ad_trainer";  
   }

  @GetMapping("/usermanage")
    public String usermanage() {
        return "admin/ad_usermanage";  
    }
  
  
}
