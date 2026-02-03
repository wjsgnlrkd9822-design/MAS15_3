package com.aloha.project.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.project.dto.CustomUser;
import com.aloha.project.dto.Pet;
import com.aloha.project.service.PetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPet(
            @RequestParam("name") String name,
            @RequestParam("species") String species,
            @RequestParam("size") String size,
            @RequestParam("age") Integer age,
            @RequestParam("weight") Float weight,
            @RequestParam("gender") String gender,
            @RequestParam("neutered") String neutered,
            @RequestParam("vaccination") String vaccination,
            @RequestParam(value = "profileImg", required = false) MultipartFile profileImg,
            @RequestParam(value = "certificateFile", required = false) MultipartFile certificateFile,
            @RequestParam(value = "etc", required = false) String etc,
            @AuthenticationPrincipal CustomUser customUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Pet pet = new Pet();
            pet.setName(name);
            pet.setSpecies(species);
            pet.setSize(size);
            pet.setAge(age);
            pet.setWeight(weight);
            pet.setGender(gender);
            pet.setNeutered(neutered);
            pet.setVaccination(vaccination);
            pet.setEtc(etc);

            if (profileImg != null && !profileImg.isEmpty()) {
                pet.setProfileImg(profileImg.getBytes());
            }

            if (certificateFile != null && !certificateFile.isEmpty()) {
                pet.setCertificateFile(certificateFile.getBytes());
            }

            // 로그인한 사용자의 PK를 ownerNo로 설정
            if (customUser != null) {
                pet.setOwnerNo(customUser.getNo());
            } else {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            petService.insert(pet);

            response.put("success", true);
            response.put("message", "반려견 등록 성공!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("반려견 등록 실패: ", e);
            response.put("success", false);
            response.put("message", "서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getPetList(@AuthenticationPrincipal CustomUser customUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (customUser == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Long ownerNo = customUser.getNo();
            List<Pet> pets = petService.selectPetsByOwnerNo(ownerNo);

            response.put("success", true);
            response.put("pets", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("반려견 목록 조회 실패: ", e);
            response.put("success", false);
            response.put("message", "서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{petNo}")
    public ResponseEntity<Pet> getPetDetail(@PathVariable("petNo") Long petNo) {
        try {
            Pet pet = petService.select(petNo);
            if (pet != null) {
                return ResponseEntity.ok(pet);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            log.error("반려견 상세 조회 실패: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePet(
            @RequestParam("petNo") Long petNo,
            @RequestParam("name") String name,
            @RequestParam("species") String species,
            @RequestParam("size") String size,
            @RequestParam("age") Integer age,
            @RequestParam("weight") Float weight,
            @RequestParam("gender") String gender,
            @RequestParam("neutered") String neutered,
            @RequestParam("vaccination") String vaccination,
            @RequestParam(value = "profileImg", required = false) MultipartFile profileImg,
            @RequestParam(value = "certificateFile", required = false) MultipartFile certificateFile,
            @RequestParam(value = "etc", required = false) String etc,
            @AuthenticationPrincipal CustomUser customUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Pet pet = new Pet();
            pet.setNo(petNo);
            pet.setName(name);
            pet.setSpecies(species);
            pet.setSize(size);
            pet.setAge(age);
            pet.setWeight(weight);
            pet.setGender(gender);
            pet.setNeutered(neutered);
            pet.setVaccination(vaccination);
            pet.setEtc(etc);

            if (profileImg != null && !profileImg.isEmpty()) {
                pet.setProfileImg(profileImg.getBytes());
            }

            if (certificateFile != null && !certificateFile.isEmpty()) {
                pet.setCertificateFile(certificateFile.getBytes());
            }

            petService.update(pet);

            response.put("success", true);
            response.put("message", "반려견 정보 수정 성공!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("반려견 수정 실패: ", e);
            response.put("success", false);
            response.put("message", "서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/mypets")
    public String myPets(Model model, @AuthenticationPrincipal CustomUser customUser) {
        try {
            Long ownerNo = customUser.getNo();
            List<Pet> pets = petService.selectPetsByOwnerNo(ownerNo);
            model.addAttribute("pets", pets);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "mypets";
    }

    /* 반려견 이미지 조회 */
    @GetMapping("/image/{petNo}")
    public ResponseEntity<byte[]> getPetImage(@PathVariable("petNo") Long petNo) {
        try {
            Pet pet = petService.selectPetImage(petNo);
            if (pet != null && pet.getProfileImg() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(pet.getProfileImg());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("반려견 이미지 조회 실패: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* 반려견 삭제 */
    @DeleteMapping("/delete/{petNo}")
    public ResponseEntity<Map<String, Object>> deletePet(
            @PathVariable("petNo") Long petNo,
            @AuthenticationPrincipal CustomUser customUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            petService.delete(petNo);
            response.put("success", true);
            response.put("message", "반려견 삭제 성공!");
        } catch (Exception e) {
            log.error("반려견 삭제 실패: ", e);
            response.put("success", false);
            response.put("message", "서버 오류: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
