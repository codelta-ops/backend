package com.give_hand.mapper;

import com.github.pagehelper.Page;
import com.give_hand.dto.TaskPageQueryDTO;
import com.give_hand.dto.TaskPageSearchDTO;
import com.give_hand.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper {
    /**
     * 分页查询任务
     * @param taskPageQueryDTO
     * @return
     */
    Page<Task> list(TaskPageQueryDTO taskPageQueryDTO);

    /**
     * 搜索任务
     * @param taskPageSearchDTO
     * @return
     */
    Page<Task> search(TaskPageSearchDTO taskPageSearchDTO);

    /**
     * 根据任务id查询任务
     * @param taskId
     * @return
     */
    @Select("SELECT * FROM task WHERE id = #{taskId}")
    Task getById(Long taskId);

    /**
     * 根据任务id查询任务图片
     * @param taskId
     * @return
     */
    @Select("SELECT image_url FROM task_image WHERE task_id = #{taskId}")
    List<String> getImageUrl(Long taskId);

    /**
     * 修改任务状态
     * @param orderId
     * @param status
     * @return
     */
    @Select("UPDATE task SET status = #{status} WHERE id = #{orderId}")
    void updateStatus(@Param("orderId") Long orderId, @Param("status") Integer status);
}
