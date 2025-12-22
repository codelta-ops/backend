package com.give_hand.controller;

import com.give_hand.vo.ChatVO;
import com.give_hand.dto.MessageSendDTO;
import com.give_hand.vo.MessageListVO;
import com.give_hand.vo.MessageSendVO;
import com.give_hand.dto.PageDTO;
import com.give_hand.result.PageResult;
import com.give_hand.result.Result;
import com.give_hand.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Slf4j
public class MessagesController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/list")
    public Result<List<MessageListVO>> list() {
        log.info("查询消息列表");
        return Result.success(messageService.list());
    }

    @GetMapping("/chat/{userId}")
    public Result<ChatVO> chat(@PathVariable Long userId) {
        log.info("查询聊天列表,{}", userId);
        ChatVO chatDTO = messageService.queryByUserId(userId);
        return Result.success(chatDTO);
    }

    @PostMapping("/send")
    public Result<MessageSendVO> send(@RequestBody MessageSendDTO messageSendDTO) {
        log.info("发送消息,{}", messageSendDTO);
        MessageSendVO messageSendVO = messageService.send(messageSendDTO);
        return Result.success(messageSendVO);
    }
}
