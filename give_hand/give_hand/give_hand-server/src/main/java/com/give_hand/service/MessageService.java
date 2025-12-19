package com.give_hand.service;

import com.give_hand.vo.ChatVO;
import com.give_hand.dto.MessageSendDTO;
import com.give_hand.vo.MessageListVO;
import com.give_hand.vo.MessageSendVO;
import com.give_hand.dto.PageDTO;
import com.give_hand.result.PageResult;

import java.util.List;

public interface MessageService {
    /**
     * 获取消息列表
     * @return
     */
    List<MessageListVO> list();

    /**
     * 发送消息
     * @param messageSendVO
     * @return
     */
    MessageSendVO send(MessageSendDTO messageSendVO);

    /**
     * 获取聊天列表
     * @param userId
     * @return
     */
    ChatVO queryByUserId(Long userId);
}
