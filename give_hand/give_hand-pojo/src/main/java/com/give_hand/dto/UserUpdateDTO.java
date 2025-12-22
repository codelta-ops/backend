package com.give_hand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private String avatar;      // 头像URL (可选)
    private String username;    // 用户名 (可选)
    private String school;      // 学校 (可选)
    private String studentId;   // 学号 (可选) —— 注意：接口写的是 studentID，但应统一为 studentId
    private String name;        // 真实姓名 (可选)
    private String email;       // 邮箱 (可选)
}
