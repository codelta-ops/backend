package com.give_hand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderListDTO {
    private String type;
    private String keyword;
    private Integer page;
    private Integer size;
}
