package com.give_hand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO {
    @NotBlank(message = "学校不能为空")
    private String school;

    @NotBlank(message = "真实姓名不能为空")
    private String name;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "学号不能为空")
    private String studentId;  // 注意：接口写的是 studentId（驼峰），但 JSON 可转下划线

    @NotBlank(message = "密码不能为空")
    private String password;
}
