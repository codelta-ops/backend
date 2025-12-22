package com.give_hand.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.give_hand.constant.StatusConstant;
import com.give_hand.constant.TaskStatusConstant;
import com.give_hand.context.BaseContext;
import com.give_hand.dto.OrderDetailDTO;
import com.give_hand.dto.OrderListDTO;
import com.give_hand.entity.Task;
import com.give_hand.entity.User;
import com.give_hand.mapper.OrderMapper;
import com.give_hand.mapper.TaskMapper;
import com.give_hand.mapper.UserMapper;
import com.give_hand.result.PageResult;
import com.give_hand.service.OrderService;
import com.give_hand.utils.SnowflakeIdGenerator;
import com.give_hand.vo.TaskPutInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Transactional
    @Override
    public void createOrder(TaskPutInfoVO taskPutInfoVO) {
        Task task = new Task();
        User user = userMapper.getById(BaseContext.getCurrentId());
        BeanUtils.copyProperties(taskPutInfoVO, task);
        task.setId(String.valueOf(SnowflakeIdGenerator.generateUid()));
        task.setUid(String.valueOf(user.getUid()));
        task.setAvatar(user.getAvatar());
        task.setUsername(user.getUsername());
        task.setImageUrl(taskPutInfoVO.getImages());
        task.setStatus(TaskStatusConstant.PENDING);
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        orderMapper.insertTask(task);
        if(task.getImageUrl() != null && !task.getImageUrl().isEmpty()){
            orderMapper.insertTaskImage(Long.valueOf(task.getId()), task.getImageUrl());
        }
    }

    @Override
    public PageResult list(OrderListDTO orderListDTO) {
        orderListDTO.setPage(orderListDTO.getPage() == null ? 1 : orderListDTO.getPage());
        orderListDTO.setSize(orderListDTO.getSize() == null ? 4 : orderListDTO.getSize());
        PageHelper.startPage(orderListDTO.getPage(), orderListDTO.getSize());
        Page<Task> page;
        if((orderListDTO.getType()).equals("published")){
            page = orderMapper.listMyTask(BaseContext.getCurrentId(), orderListDTO.getKeyword());
        }else if ((orderListDTO.getType()).equals("accepted")){
            page = orderMapper.listOtherTask(BaseContext.getCurrentId(), orderListDTO.getKeyword());
        }else{
            page = null;
        }
        return new PageResult(page.getResult(), page.getTotal());
    }

    @Override
    public OrderDetailDTO getOrder(Long orderId) {
        Task task = taskMapper.getById(orderId);
        return OrderDetailDTO.builder()
                .avatar(task.getAvatar())
                .username(task.getUsername())
                .price(task.getPrice())
                .title(task.getTitle())
                .content(task.getContent())
                .status(task.getStatus())
                .uid(task.getUid())
                .build();
    }

    @Override
    public Integer changeStatus(Long orderId, Integer status) {
        taskMapper.updateStatus(orderId, TaskStatusConstant.IN_PROGRESS);
        return status;
    }

    @Override
    public void cancel(Long orderId) {
        orderMapper.deleteOrderById(orderId);
        orderMapper.deleteOrderImageById(orderId);
    }
}
