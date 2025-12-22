package com.give_hand.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginVO {
    private String token;
    private String avatar;      // 头像
    private String username;    // 用户名
    private String school;      // 学校
    private String studentId;   // 学号
    private String name;        // 姓名
    private String email;       // 邮箱
}
