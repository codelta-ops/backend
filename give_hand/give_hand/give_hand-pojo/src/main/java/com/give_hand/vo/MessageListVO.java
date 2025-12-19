package com.give_hand.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageListVO {
    private String avatar;
    private String username;
    private LocalDateTime time;
    private Integer count;
    private String latestMsg;
    private String uId;
}
