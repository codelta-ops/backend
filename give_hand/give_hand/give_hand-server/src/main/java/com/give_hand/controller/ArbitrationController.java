package com.give_hand.controller;

import com.give_hand.dto.ArbitrationApplyDTO;
import com.give_hand.result.PageResult;
import com.give_hand.result.Result;
import com.give_hand.service.ArbitrationService;
import com.give_hand.dto.PageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/arbitration")
@Slf4j
public class ArbitrationController {
    @Autowired
    private ArbitrationService arbitrationService;

    @GetMapping("/list")
    public Result<PageResult> list(PageDTO pageDTO) {
        log.info("查询仲裁列表,{}", pageDTO);
        return Result.success(arbitrationService.list(pageDTO));
    }

    @PostMapping("/apply")
    public Result apply(@RequestBody ArbitrationApplyDTO arbitrationApplyDTO) {
        log.info("申请仲裁,{}", arbitrationApplyDTO);
        arbitrationService.apply(arbitrationApplyDTO);
        return Result.success();
    }
}
