package com.give_hand.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskInfoVO {
    private Long id;
    private String url;
    private String title;
    private String avatar;
    private String username;
    private Long price;
}
