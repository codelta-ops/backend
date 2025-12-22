package com.give_hand.service;

import com.give_hand.dto.ArbitrationApplyDTO;
import com.give_hand.result.PageResult;
import com.give_hand.dto.PageDTO;
import org.apache.ibatis.annotations.Select;

public interface ArbitrationService {
    /**
     * 获取仲裁列表
     * @return
     */
    PageResult list(PageDTO pageDTO);

    /**
     * 申请仲裁
     * @param arbitrationApplyDTO
     */
    void apply(ArbitrationApplyDTO arbitrationApplyDTO);
}
