package com.give_hand.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.give_hand.constant.TaskStatusConstant;
import com.give_hand.context.BaseContext;
import com.give_hand.dto.ArbitrationApplyDTO;
import com.give_hand.entity.Arbitration;
import com.give_hand.entity.Task;
import com.give_hand.mapper.ArbitrationMapper;
import com.give_hand.mapper.TaskMapper;
import com.give_hand.result.PageResult;
import com.give_hand.service.ArbitrationService;
import com.give_hand.dto.PageDTO;
import com.give_hand.vo.ArbitrationListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArbitrationServiceImpl implements ArbitrationService {
    @Autowired
    private ArbitrationMapper arbitrationMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public PageResult list(PageDTO pageDTO) {
        int page = (pageDTO.getPage() == null || pageDTO.getPage() <= 0) ? 1 : pageDTO.getPage();
        int size = (pageDTO.getSize() == null || pageDTO.getSize() <= 0 || pageDTO.getSize() > 100) ? 8 : pageDTO.getSize();
        PageHelper.startPage(page, size);
        Page<Arbitration> pages = arbitrationMapper.list();
        List<ArbitrationListVO> list = new ArrayList<>();
        if (pages != null && pages.getTotal() > 0) {
            for (Arbitration a : pages) {
                Long id = Long.valueOf(a.getOrderId());
                Task task = taskMapper.getById(id);
                if(task.getUid().equals(String.valueOf(BaseContext.getCurrentId()))){
                    List<String> imageUrl = taskMapper.getImageUrl(id);
                    ArbitrationListVO arbitrationListVO = ArbitrationListVO.builder()
                            .id(id)
                            .username(task.getUsername())
                            .avatar(task.getAvatar())
                            .title(task.getTitle())
                            .price(task.getPrice())
                            .status(task.getStatus())
                            .build();
                    if(imageUrl != null && !imageUrl.isEmpty()){
                        arbitrationListVO.setUrl(imageUrl.get(0));
                    }
                    list.add(arbitrationListVO);
                }
            }
        }
        return new PageResult(list, pages.getTotal());
    }

    @Transactional
    @Override
    public void apply(ArbitrationApplyDTO arbitrationApplyDTO) {
        arbitrationMapper.insertArbitration(Arbitration.builder()
                .orderId(String.valueOf(arbitrationApplyDTO.getOrderId()))
                .reason(arbitrationApplyDTO.getReason())
                .createTime(LocalDateTime.now())
                .build());
        if(arbitrationApplyDTO.getEvidence() != null && !arbitrationApplyDTO.getEvidence().isEmpty()){
            arbitrationMapper.insertArbitrationEvidence(arbitrationApplyDTO.getOrderId(), arbitrationApplyDTO.getEvidence());
        }
        taskMapper.updateStatus(arbitrationApplyDTO.getOrderId(), TaskStatusConstant.ARBITRATION);
    }
}
