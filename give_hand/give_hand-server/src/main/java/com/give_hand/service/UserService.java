package com.give_hand.service;

import com.give_hand.dto.UserInfoDTO;
import com.give_hand.dto.UserLoginDTO;
import com.give_hand.dto.UserRegisterDTO;
import com.give_hand.entity.User;
import com.give_hand.vo.UserInfoVO;

public interface UserService {

    /**
     * 员工登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * 员工注册
     * @param userRegisterDTO
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 获取当前登录用户的信息
     * @return
     */
    UserInfoVO getCurrentUserInfo();

    /**
     * 修改用户信息
     * @param userInfoDTO
     */
    void update(UserInfoDTO userInfoDTO);
}
