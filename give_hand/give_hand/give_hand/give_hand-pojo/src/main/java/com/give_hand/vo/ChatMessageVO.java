package com.give_hand.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageVO {
    private String msgId;
    private String uid;
    private String content;
    private Boolean isMine;
    private LocalDateTime time;
}
