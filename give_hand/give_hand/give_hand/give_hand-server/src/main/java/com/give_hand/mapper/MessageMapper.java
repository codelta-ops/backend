package com.give_hand.mapper;

import com.github.pagehelper.Page;
import com.give_hand.dto.PageDTO;
import com.give_hand.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MessageMapper {
    /**
     * 获取所有消息
     * @return
     */
    @Select("select * from message where another_id = #{anotherId}")
    Page<Message> list(Long anotherId);

    /**
     * 添加消息
     * @param message
     */
    @Select("insert into message(msg_id, uid, content, time, status, another_id) values(#{msgId}, #{uid}, #{content}, #{time}, #{status}, #{anotherId})")
    void insert(Message message);

    /**
     * 根据用户id查询消息
     * @param userId
     * @return
     */
    @Select("select * from message where uid = #{userId} and another_id = #{anotherId} order by time asc")
    List<Message> queryByUserId(@Param("userId") Long userId, @Param("anotherId") Long anotherId);

    /**
     * 设置消息已读
     * @param msgId
     */
    @Update("update message set status = #{status} where msg_id = #{msg_id}")
    void setStatus(@Param("msg_id") Long msgId, @Param("status") Integer status);
}
