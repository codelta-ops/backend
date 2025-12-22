package com.give_hand.controller;

import com.give_hand.result.Result;
import com.give_hand.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
@Slf4j
public class SettingController {
    @Autowired
    private SettingService settingService;

    @PutMapping("/update")
    public Result update(Boolean notifications) {
        log.info("更新设置,{}", notifications);
        settingService.update(notifications);
        return Result.success();
    }
}
