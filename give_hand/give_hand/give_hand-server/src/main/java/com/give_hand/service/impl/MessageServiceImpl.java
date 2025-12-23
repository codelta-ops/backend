package com.give_hand.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.give_hand.constant.MessageStatusConstant;
import com.give_hand.context.BaseContext;
import com.give_hand.entity.User;
import com.give_hand.mapper.UserMapper;
import com.give_hand.vo.*;
import com.give_hand.dto.MessageSendDTO;
import com.give_hand.dto.PageDTO;
import com.give_hand.entity.Message;
import com.give_hand.mapper.MessageMapper;
import com.give_hand.result.PageResult;
import com.give_hand.service.MessageService;
import com.give_hand.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<MessageListVO> list() {
        Long currentId = BaseContext.getCurrentId();
        List<Message> messages = messageMapper.list(currentId);
        MessageListVO messageListVO = null;
        Map<Long,MessageListVO> map = new HashMap<>();
        Integer count = null;
        for(Message message: messages){
            Long partnerId = Objects.equals(message.getUid(), currentId) ? message.getAnotherId() : message.getUid();
            User user = userMapper.getById(partnerId);
            if(Objects.equals(message.getStatus(), MessageStatusConstant.UNREAD) && !Objects.equals(message.getUid(), currentId)){
                count = 1;
            }else{
                count = 0;
            }
            if (!map.containsKey(partnerId)) {
                messageListVO = MessageListVO.builder()
                        .avatar(user.getAvatar())
                        .username(user.getUsername())
                        .time(message.getTime())
                        .count(count)
                        .latestMsg(message.getContent())
                        .uid(String.valueOf(partnerId))
                        .build();
                map.put(partnerId, messageListVO);
            } else {
                messageListVO = map.get(partnerId);
                messageListVO.setCount(messageListVO.getCount() + count);
                map.put(partnerId, messageListVO);
            }
        }
        return map.values().stream()
                .sorted(Comparator.comparing(MessageListVO::getTime).reversed())
                .toList();
    }

    @Override
    public MessageSendVO send(MessageSendDTO messageSendDTO) {
        Message message = Message.builder()
                .msgId(SnowflakeIdGenerator.generateUid())
                .uid(BaseContext.getCurrentId())
                .content(messageSendDTO.getContent())
                .time(LocalDateTime.now())
                .status(MessageStatusConstant.UNREAD)
                .anotherId(messageSendDTO.getReceiverId())
                .build();
        messageMapper.insert(message);
        return MessageSendVO.builder()
                .msgId(message.getMsgId())
                .time(message.getTime())
                .content(message.getContent())
                .build();
    }

    @Override
    public ChatVO queryByUserId(Long userId) {
        User user = userMapper.getById(userId);
        OtherUserVO otherUser = OtherUserVO.builder()
                .userUid(user.getUid())
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .build();
        List<Message> message = messageMapper.queryByUserId(userId, BaseContext.getCurrentId());
        for(Message m: message){
            messageMapper.setStatus(m.getMsgId(), MessageStatusConstant.READ);
        }
        message.addAll(messageMapper.queryByUserId(BaseContext.getCurrentId(), userId));
        message.sort(Comparator.comparing(Message::getTime));
        System.out.println( message);
        List<ChatMessageVO> chatMessageVO = message.stream().map(m -> ChatMessageVO.builder()
                .msgId(String.valueOf(m.getMsgId()))
                .uid(String.valueOf(m.getUid()))
                .content(m.getContent())
                .isMine(m.getUid().equals(BaseContext.getCurrentId()))
                .time(m.getTime())
                .build()).toList();
        return ChatVO.builder()
                .otherUser(otherUser)
                .messages(chatMessageVO)
                .build();
    }
}
