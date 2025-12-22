package com.give_hand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    private String avatar;
    private String username;
    private Long price;
    private String url;
    private String title;
    private Integer status;
    private String content;
    private String uid;
}
