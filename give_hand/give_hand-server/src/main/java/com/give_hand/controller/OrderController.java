package com.give_hand.controller;

import com.give_hand.constant.TaskStatusConstant;
import com.give_hand.dto.OrderDetailDTO;
import com.give_hand.dto.OrderListDTO;
import com.give_hand.result.PageResult;
import com.give_hand.result.Result;
import com.give_hand.service.OrderService;
import com.give_hand.vo.TaskPutInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PutMapping("/create")
    public Result createOrder(@RequestBody TaskPutInfoVO taskPutInfoVO) {
        log.info("创建订单：标题：{}，价格：{}，内容：{}", taskPutInfoVO.getTitle(), taskPutInfoVO.getPrice(), taskPutInfoVO.getContent());
        orderService.createOrder(taskPutInfoVO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<PageResult> list(OrderListDTO orderListDTO) {
        log.info("查询订单列表,{}", orderListDTO);
        PageResult pageResult = orderService.list(orderListDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{orderId}")
    public Result<OrderDetailDTO> getOrder(@PathVariable Long orderId) {
        log.info("查询订单：{}", orderId);
        OrderDetailDTO orderDetailDTO = orderService.getOrder(orderId);
        return Result.success(orderDetailDTO);
    }

    @PutMapping("/{orderId}/accept")
    public Result<Integer> acceptOrder(@PathVariable Long orderId) {
        log.info("接受订单：{}", orderId);
        Integer status = orderService.changeStatus(orderId, TaskStatusConstant.IN_PROGRESS);
        return Result.success(status);
    }

    @PutMapping("/{orderId}/complete")
    public Result<Integer> completeOrder(@PathVariable Long orderId) {
        log.info("完成订单：{}", orderId);
        Integer status = orderService.changeStatus(orderId, TaskStatusConstant.COMPLETED);
        return Result.success(status);
    }

    @PostMapping("/{orderId}/cancel")
    public Result cancelOrder(@PathVariable Long orderId) {
        log.info("取消订单：{}", orderId);
        orderService.cancel(orderId);
        return Result.success();
    }

    @DeleteMapping("/{orderId}/delete")
    public Result deleteOrder(@PathVariable Long orderId) {
        log.info("删除订单：{}", orderId);
        orderService.cancel(orderId);
        return Result.success();
    }

}
