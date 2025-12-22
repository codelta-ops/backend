package com.give_hand.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SettingMapper {
    /**
     * 更新设置
     * @param status
     */
    @Update("update setting set notifications = #{status}")
    void update(Integer status);

    /**
     * 插入设置
     * @param uid
     */
    @Update("insert into setting (uid, notifications) values (#{uid}, 1)")
    void insert(Long uid);
}
