package com.give_hand.service;

import com.give_hand.dto.TaskPageQueryDTO;
import com.give_hand.dto.TaskPageSearchDTO;
import com.give_hand.result.PageResult;
import com.give_hand.vo.TaskDetailVO;

public interface TaskService {
    /**
     * 获取任务列表
     * @param taskPageQueryDTO
     * @return
     */
    PageResult list(TaskPageQueryDTO taskPageQueryDTO);

    /**
     * 搜索任务
     * @param taskPageSearchDTO
     * @return
     */
    PageResult search(TaskPageSearchDTO taskPageSearchDTO);

    /**
     * 获取任务详情
     * @param taskId
     * @return
     */
    TaskDetailVO getById(Long taskId);
}
