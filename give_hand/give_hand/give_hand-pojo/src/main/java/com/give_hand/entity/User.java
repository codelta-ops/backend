package com.give_hand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private Long uid;           // 用户唯一 UID
    private String avatar;      // 头像
    private String username;    // 用户名
    private String school;      // 学校
    private String studentId;   // 学号
    private String name;        // 姓名
    private String email;       // 邮箱
    private String password;    // 密码
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
