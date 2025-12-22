package com.give_hand.mapper;

import com.give_hand.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名或邮箱查询用户
     * @param account
     * @return
     */
    @Select("select * from user where username = #{account} or email = #{account}")
    User getByAccount(String account);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Select("select * from user where uid = #{id}")
    User getById(Long id);

    /**
     * 新增用户
     * @param user
     */
    @Select("insert into user (uid, avatar, username, school, student_id, name, email, password, status, create_time, update_time) " +
            "values (#{uid}, #{avatar}, #{username}, #{school}, #{studentId}, #{name}, #{email}, #{password}, #{status}, #{createTime}, #{updateTime})")
    void insert(User user);

    /**
     * 修改用户
     * @param user
     */
    void update(User user);
}
