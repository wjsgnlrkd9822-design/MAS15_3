package com.aloha.project.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Long no; 
    @Builder.Default
    private String id = UUID.randomUUID().toString();  

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(
      regexp = "^[A-Za-z0-9]{6,20}$",
      message = "아이디는 영문과 숫자만 가능하며 6~20자 이내로 작성해야합니다."
    )
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message = "비밀번호는 8자 이상이며, 영문, 숫자, 특수문자를 포함해야합니다."
    )
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 10, message = "이름은 2~10글자 이내로 작성해야합니다.")
    private String name;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
    @NotBlank(message = "상세 주소를 입력해주세요.")
    private String detailAddress;
    @NotBlank(message = "생년월일을 입력해주세요.")
    private String birth;
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    private Date createdAt;
    private Date updatedAt;
    private int enabled;

    private List<UserAuth> authList;

    public User() {
        this.id = UUID.randomUUID().toString();
    }
    
    

    
}
