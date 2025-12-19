package com.give_hand.service;

import com.give_hand.dto.OrderDetailDTO;
import com.give_hand.dto.OrderListDTO;
import com.give_hand.result.PageResult;
import com.give_hand.vo.TaskPutInfoVO;

public interface OrderService {
    /**
     * 创建订单
     * @param taskPutInfoVO
     */
    void createOrder(TaskPutInfoVO taskPutInfoVO);

    /**
     * 获取订单列表
     * @return
     */
    PageResult list(OrderListDTO orderListDTO);

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderDetailDTO getOrder(Long orderId);

    /**
     * 接受订单
     * @param orderId
     * @return
     */
    Integer changeStatus(Long orderId, Integer status);

    /**
     * 取消订单
     * @param orderId
     */
    void cancel(Long orderId);
}
