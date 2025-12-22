package com.give_hand.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.give_hand.dto.TaskPageQueryDTO;
import com.give_hand.dto.TaskPageSearchDTO;
import com.give_hand.entity.Task;
import com.give_hand.entity.User;
import com.give_hand.mapper.TaskMapper;
import com.give_hand.mapper.UserMapper;
import com.give_hand.result.PageResult;
import com.give_hand.service.TaskService;
import com.give_hand.vo.TaskDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult list(TaskPageQueryDTO taskPageQueryDTO) {
        //设置默认值
        taskPageQueryDTO.setPage(taskPageQueryDTO.getPage() == null ? 1 : taskPageQueryDTO.getPage());
        taskPageQueryDTO.setSize(taskPageQueryDTO.getSize() == null ? 8 : taskPageQueryDTO.getSize());
        PageHelper.startPage(taskPageQueryDTO.getPage(), taskPageQueryDTO.getSize());
        Page<Task> page = taskMapper.list(taskPageQueryDTO);
        return new PageResult(page.getResult(), page.getTotal());
    }

    @Override
    public PageResult search(TaskPageSearchDTO taskPageSearchDTO) {
        taskPageSearchDTO.setPage(taskPageSearchDTO.getPage() == null ? 1 : taskPageSearchDTO.getPage());
        taskPageSearchDTO.setSize(taskPageSearchDTO.getSize() == null ? 8 : taskPageSearchDTO.getSize());
        PageHelper.startPage(taskPageSearchDTO.getPage(), taskPageSearchDTO.getSize());
        Page<Task> page = taskMapper.search(taskPageSearchDTO);
        return new PageResult(page.getResult(), page.getTotal());
    }

    @Override
    public TaskDetailVO getById(Long taskId) {
        Task task = taskMapper.getById(taskId);
        return TaskDetailVO.builder()
                .avatar(task.getAvatar())
                .username(task.getUsername())
                .price(task.getPrice())
                .url(taskMapper.getImageUrl(taskId))
                .title(task.getTitle())
                .content(task.getContent())
                .build();
    }
}
