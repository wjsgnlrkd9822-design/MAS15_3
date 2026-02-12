package com.aloha.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.project.dto.HotelRoom;
import com.aloha.project.dto.HotelService;
import com.aloha.project.dto.MonthlySalesDto;
import com.aloha.project.dto.Notice;
import com.aloha.project.dto.PetStatus;
import com.aloha.project.dto.Trainer;
import com.aloha.project.dto.User;
import com.aloha.project.dto.userTotalSales;
import com.aloha.project.service.AddtionalService;
import com.aloha.project.service.FileService;
import com.aloha.project.service.NoticeService;
import com.aloha.project.service.PetStatusService;
import com.aloha.project.service.ReservationService;
import com.aloha.project.service.RoomService;
import com.aloha.project.service.TrainerService;
import com.aloha.project.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final NoticeService noticeService;
    private final TrainerService trainerService;
    private final AddtionalService addtionalService;
    private final RoomService roomService;
    private final FileService fileService;
    private final UserService userService;
    private final ReservationService reservationService;
    private final PetStatusService petStatusService; // ⭐ 추가
    
    @GetMapping("")
    public String admin(Model model) {

        Long totalSales = reservationService.getTotalSales();
        List<userTotalSales> memberSales = reservationService.getMemberTotalSales();
        List<MonthlySalesDto> monthlySales = reservationService.getMonthlySales();

        model.addAttribute("totalSales", totalSales);
        model.addAttribute("memberSales", memberSales);
        model.addAttribute("monthlySales", monthlySales);

        return "admin/ad_main";
    }


    @GetMapping("/service")
    public String service(Model model) {
        try {
            List<HotelService> serviceList = addtionalService.list();
            List<HotelRoom> roomList = roomService.list();
            model.addAttribute("serviceList", serviceList);                         
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
            System.out.println("DB 조회 결과: " + room);
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
            String savedFileName = fileService.save(imgFile);
            return ResponseEntity.ok(savedFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{roomNo}")
    @ResponseBody
    public ResponseEntity<?> updateRoom(
            @PathVariable("roomNo") Long roomNo,
            @RequestBody HotelRoom hotelRoom) {
        try {
            hotelRoom.setRoomNo(roomNo);
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
    @DeleteMapping("/room/delete/{roomNo}") 
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

    @GetMapping("/serviceupdate")
    public String serviceUpdate(@RequestParam("serviceNo") Long serviceNo, Model model) {
        try {
            HotelService service = addtionalService.select(serviceNo);
            System.out.println("DB 조회 결과: " + service);
            model.addAttribute("service", service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/ad_serviceupdate";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update/{serviceNo}")
    @ResponseBody
    public ResponseEntity<?> updateService(
            @PathVariable Long serviceNo,
            @RequestBody HotelService hotelService) {
        try {
            hotelService.setServiceNo(serviceNo);
            boolean result = addtionalService.update(hotelService);
            if (!result) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/service/delete/{serviceNo}")
    public ResponseEntity<?> deleteService(@PathVariable("serviceNo") Long serviceNo) {   
        try {
            boolean result = addtionalService.delete(serviceNo);
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

    @PostMapping("/noticeadd")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<String> noticeadd(@RequestBody Notice notice) {
        try {
            boolean result = noticeService.insert(notice);
            if (!result) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            } 
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/trainer") 
    public String trainer(Model model) throws Exception {
        List<Trainer> trainerList = trainerService.list();
        model.addAttribute("trainerList", trainerList);
        return "admin/ad_trainer";  
    }

    @GetMapping("/trainerinsert")
    public String trainerinsert() {
        return "admin/ad_trainerinsert";  
    } 

    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")   
    @PostMapping("/traineradd")
    public ResponseEntity<String> traineradd(@RequestBody Trainer Trainer) throws Exception {
        try {  
            boolean result = trainerService.insert(Trainer);
            if (!result) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            } 
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);  
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }                                     
    }

    @GetMapping("/trainerupdate")
    public String trainerupdate(@RequestParam("trainerNo") Long trainerNo, Model model) {
        try {
            Trainer trainer = trainerService.select(trainerNo);
            model.addAttribute("trainer", trainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/ad_trainerupdate";
    }

    @PutMapping("/trainerupdate/{trainerNo}")
    public ResponseEntity<?> updateTrainer(
            @PathVariable("trainerNo") Long trainerNo,
            @RequestBody Trainer trainer) {
        try {
            trainer.setTrainerNo(trainerNo);
            boolean result = trainerService.update(trainer);
            if (!result) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/trainer/delete/{trainerNo}")
    public ResponseEntity<?> deleteTrainer(@PathVariable("trainerNo") Long trainerNo) {   
        try {
            boolean result = trainerService.delete(trainerNo);
            if (!result) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usermanage")
    public String usermanage(Model model) throws Exception {
        List<User> userList = userService.list();
        model.addAttribute("userList", userList);
        return "admin/ad_usermanage";  
    }

    @PreAuthorize("hasRole('ADMIN')")  
    @DeleteMapping("/user/delete/{no}")
    public ResponseEntity<?> deleteUser(@PathVariable("no") Long no) {   
        try {
            int result = userService.deleteByNO(no);
            if (result == 0) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    /**
     * ----------------------------반려견 상태 관리 페이지----------------------------
     */
    @GetMapping("/petstatus")
    public String petStatusPage(Model model) {
        try {
            List<Map<String, Object>> petList = petStatusService.getActiveReservationsWithPetStatus();
            model.addAttribute("petList", petList);
            System.out.println("✅ 조회된 반려견 수: " + (petList != null ? petList.size() : 0));
            
            // 디버깅용 - 첫 번째 데이터 출력
            if (petList != null && !petList.isEmpty()) {
                System.out.println("첫 번째 반려견 정보: " + petList.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ 반려견 목록 조회 실패: " + e.getMessage());
            model.addAttribute("petList", List.of());
        }
        return "admin/ad_petstatus";
    }

    /**
     * 반려견 상태 수정 (AJAX)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/petstatus/update/{petNo}")
    @ResponseBody
    public ResponseEntity<?> updatePetStatus(
            @PathVariable("petNo") Long petNo,  // ⭐ ("petNo") 명시!
            @RequestBody Map<String, String> request) {
        try {
            System.out.println("=== 반려견 상태 업데이트 요청 ===");
            System.out.println("petNo: " + petNo);
            System.out.println("status: " + request.get("status"));
            System.out.println("nextSchedule: " + request.get("nextSchedule"));
            
            // PetStatus 객체 생성
            PetStatus petStatus = new PetStatus();
            petStatus.setPetNo(petNo);
            petStatus.setStatus(request.get("status"));
            petStatus.setNextSchedule(request.get("nextSchedule"));
            
            // 기존 상태가 있는지 확인
            PetStatus existing = petStatusService.getPetStatus(petNo);
            boolean result;
            
            if (existing == null) {
                // 없으면 INSERT
                System.out.println("→ 새로운 상태 등록");
                result = petStatusService.insertPetStatus(petStatus);
            } else {
                // 있으면 UPDATE
                System.out.println("→ 기존 상태 업데이트");
                result = petStatusService.updatePetStatus(petStatus);
            }
            
            System.out.println("처리 결과: " + (result ? "✅ 성공" : "❌ 실패"));
            
            if (!result) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ 에러 발생: " + e.getMessage());
            return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 반려견 상태 삭제 (AJAX)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/petstatus/delete/{petNo}")
    @ResponseBody
    public ResponseEntity<?> deletePetStatus(@PathVariable("petNo") Long petNo) { 
        try {
            boolean result = petStatusService.deletePetStatus(petNo);
            if (!result) {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * ----------------------------반려견 상태 관리 페이지----------------------------
     */


}
