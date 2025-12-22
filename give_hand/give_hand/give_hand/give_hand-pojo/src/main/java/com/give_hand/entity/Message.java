package com.give_hand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Long msgId;
    private Long uid;
    private String content;
    private LocalDateTime time;
    private Integer status;
    private Long anotherId;
}
