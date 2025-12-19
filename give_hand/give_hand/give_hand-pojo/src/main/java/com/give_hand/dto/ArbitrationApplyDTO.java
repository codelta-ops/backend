package com.give_hand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArbitrationApplyDTO {
    private Long orderId;
    private String reason;
    private List<String> evidence;
}
