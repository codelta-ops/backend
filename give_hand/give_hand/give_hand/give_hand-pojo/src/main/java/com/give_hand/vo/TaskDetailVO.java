package com.give_hand.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDetailVO {
    private String avatar;
    private String username;
    private Long price;
    private List<String> url;
    private String title;
    private String content;
}
