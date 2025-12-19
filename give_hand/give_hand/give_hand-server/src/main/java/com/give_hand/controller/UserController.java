package com.give_hand.controller;

import com.give_hand.constant.JwtClaimsConstant;
import com.give_hand.context.BaseContext;
import com.give_hand.dto.UserInfoDTO;
import com.give_hand.dto.UserLoginDTO;
import com.give_hand.dto.UserRegisterDTO;
import com.give_hand.entity.User;
import com.give_hand.properties.JwtProperties;
import com.give_hand.result.Result;
import com.give_hand.service.UserService;
import com.give_hand.utils.JwtUtil;
import com.give_hand.vo.UserInfoVO;
import com.give_hand.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/auth/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);

        User user = userService.login(userLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUid());
        claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
        claims.put(JwtClaimsConstant.NAME, user.getName());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .school(user.getSchool())
                .studentId(user.getStudentId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * 注册
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/auth/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.success();
    }

    @GetMapping("/user/info")
    public Result<UserInfoVO> info() {
        log.info("获取当前登录用户的信息");
        UserInfoVO userInfoVO = userService.getCurrentUserInfo();
        return Result.success(userInfoVO);
    }

    @PutMapping("/user/update")
    public Result update(@RequestBody UserInfoDTO userInfoDTO) {
        log.info("更新用户信息：{}", userInfoDTO);
        userService.update(userInfoDTO);
        return Result.success();
    }

    @PostMapping("/user/logout")
    public Result logout() {
        log.info("用户退出，{}", BaseContext.getCurrentId());
        BaseContext.removeCurrentId();
        return Result.success();
    }

}
