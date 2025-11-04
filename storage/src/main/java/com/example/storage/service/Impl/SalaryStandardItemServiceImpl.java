package com.example.storage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.SalaryStandardItem;
import com.example.storage.mapper.SalaryStandardItemMapper;
import com.example.storage.service.SalaryStandardItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 薪酬标准明细表 Service 实现类
 */
@Service
public class SalaryStandardItemServiceImpl extends ServiceImpl<SalaryStandardItemMapper, SalaryStandardItem> implements SalaryStandardItemService {

    @Override
    public List<SalaryStandardItem> getByStandardId(Long standardId) {
        LambdaQueryWrapper<SalaryStandardItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryStandardItem::getStandardId, standardId);
        return list(wrapper);
    }

    @Override
    public boolean deleteByStandardId(Long standardId) {
        LambdaQueryWrapper<SalaryStandardItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryStandardItem::getStandardId, standardId);
        return remove(wrapper);
    }
}

