package com.give_hand.mapper;

import com.github.pagehelper.Page;
import com.give_hand.entity.Arbitration;
import com.give_hand.vo.ArbitrationListVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface ArbitrationMapper {
    /**
     * 获取仲裁列表
     * @return
     */
    @Select("select * from arbitration")
    Page<Arbitration> list();

    /**
     * 添加仲裁
     * @param arbitration
     */
    @Insert("insert into arbitration(order_id,create_time, reason) values(#{orderId}, #{createTime}, #{reason})")
    void insertArbitration(Arbitration arbitration);

    /**
     * 添加仲裁证据
     * @param arbitrationId
     * @param evidence
     */
    void insertArbitrationEvidence(@Param("arbitrationId") Long arbitrationId, @Param("evidence") List<String> evidence);
}
