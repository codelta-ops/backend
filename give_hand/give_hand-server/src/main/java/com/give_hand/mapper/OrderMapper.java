package com.give_hand.mapper;

import com.github.pagehelper.Page;
import com.give_hand.dto.OrderListDTO;
import com.give_hand.entity.Task;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 创建订单
     * @param task
     */
    @Insert("INSERT INTO task (id, uid, avatar, username, title, content, price, status)" +
            "VALUES (#{id}, #{uid}, #{avatar}, #{username}, #{title}, #{content}, #{price}, #{status})")
    void insertTask(Task task);

    /**
     * 创建订单图片
     * @param taskId
     * @param imageUrl
     */
    void insertTaskImage(@Param("taskId") Long taskId, @Param("imageUrl") List<String> imageUrl);

    /**
     * 获取我的订单列表
     * @param uid
     * @param keyword
     * @return
     */
    Page<Task> listMyTask(@Param("uid") Long uid, @Param("keyword") String keyword);

    /**
     * 获取其他用户订单列表
     * @param uid
     * @param keyword
     * @return
     */
    Page<Task> listOtherTask(@Param("uid") Long uid, @Param("keyword") String keyword);

    /**
     * 删除订单
     * @param orderId
     */
    @Delete("DELETE FROM task WHERE id = #{orderId}")
    void deleteOrderById(Long orderId);

    /**
     * 删除订单图片
     * @param orderId
     */
    @Delete("DELETE FROM task_image WHERE task_id = #{orderId}")
    void deleteOrderImageById(Long orderId);
}
