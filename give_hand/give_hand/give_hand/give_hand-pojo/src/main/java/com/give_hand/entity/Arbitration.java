package com.give_hand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Arbitration {
    private Long id;
    private String orderId;
    private LocalDateTime createTime;
    public String reason;
}
