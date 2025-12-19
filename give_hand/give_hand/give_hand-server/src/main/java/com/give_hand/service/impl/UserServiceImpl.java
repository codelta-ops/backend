package com.give_hand.service.impl;

import com.give_hand.constant.MessageConstant;
import com.give_hand.constant.StatusConstant;
import com.give_hand.context.BaseContext;
import com.give_hand.dto.UserInfoDTO;
import com.give_hand.dto.UserLoginDTO;
import com.give_hand.dto.UserRegisterDTO;
import com.give_hand.entity.User;
import com.give_hand.exception.AccountLockedException;
import com.give_hand.exception.AccountNotFoundException;
import com.give_hand.exception.PasswordErrorException;
import com.give_hand.mapper.SettingMapper;
import com.give_hand.mapper.UserMapper;
import com.give_hand.service.UserService;
import com.give_hand.utils.SnowflakeIdGenerator;
import com.give_hand.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SettingMapper settingMapper;

    /**
     * 员工登录
     *
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getAccount();
        String password = userLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        User user = userMapper.getByAccount(account);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传过来的密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (user.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return user;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setUid(SnowflakeIdGenerator.generateUid());
        user.setStatus(StatusConstant.ENABLE);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
        settingMapper.insert(user.getUid());
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        User user = userMapper.getById(BaseContext.getCurrentId());
        return UserInfoVO.builder()
                .uid(user.getUid())
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .school(user.getSchool())
                .studentId(user.getStudentId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public void update(UserInfoDTO userInfoDTO) {
        User user = userMapper.getById(BaseContext.getCurrentId());
        user.setAvatar(userInfoDTO.getAvatar());
        user.setUsername(userInfoDTO.getUsername());
        user.setSchool(userInfoDTO.getSchool());
        user.setStudentId(userInfoDTO.getStudentId());
        user.setName(userInfoDTO.getName());
        user.setEmail(userInfoDTO.getEmail());
        userMapper.update(user);
    }

}
