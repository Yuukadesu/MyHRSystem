package com.example.storage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.Position;
import com.example.storage.mapper.PositionMapper;
import com.example.storage.service.PositionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 职位表 Service 实现类
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Override
    public List<Position> getByThirdOrgId(Long thirdOrgId) {
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Position::getThirdOrgId, thirdOrgId);
        return list(wrapper);
    }

    @Override
    public Position getByPositionName(String positionName) {
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Position::getPositionName, positionName);
        return getOne(wrapper);
    }
}

