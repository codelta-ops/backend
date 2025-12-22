package com.give_hand.controller;

import com.give_hand.dto.TaskPageQueryDTO;
import com.give_hand.dto.TaskPageSearchDTO;
import com.give_hand.result.PageResult;
import com.give_hand.result.Result;
import com.give_hand.service.TaskService;
import com.give_hand.vo.TaskDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务管理
 */
@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/list")
    public Result<PageResult> list(TaskPageQueryDTO taskPageQueryDTO) {
        log.info("分页查询任务：页码{}，大小：{}", taskPageQueryDTO.getPage(), taskPageQueryDTO.getSize());
        return Result.success(taskService.list(taskPageQueryDTO));
    }

    @GetMapping("/search")
    public Result<PageResult> search(TaskPageSearchDTO taskPageSearchDTO) {
        log.info("分页搜索任务：页码{}，大小：{}，关键字：{}", taskPageSearchDTO.getPage(), taskPageSearchDTO.getSize(), taskPageSearchDTO.getKeyword());
        return Result.success(taskService.search(taskPageSearchDTO));
    }

    @GetMapping("/{taskId}")
    public Result<TaskDetailVO> getById(@PathVariable Long taskId) {
        log.info("查询任务：{}", taskId);
        return Result.success(taskService.getById(taskId));
    }
}
