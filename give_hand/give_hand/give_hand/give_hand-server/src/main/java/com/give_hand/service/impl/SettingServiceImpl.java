package com.give_hand.service.impl;

import com.give_hand.constant.SettingConstant;
import com.give_hand.mapper.SettingMapper;
import com.give_hand.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl implements SettingService {
    @Autowired
    private SettingMapper settingMapper;
    @Override
    public void update(Boolean notifications) {
        settingMapper.update(notifications ? SettingConstant.ENABLE_NOTIFICATIONS : SettingConstant.DISABLE_NOTIFICATIONS);
    }
}
