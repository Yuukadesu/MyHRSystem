package com.example.storage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.SalaryStandard;
import com.example.storage.mapper.SalaryStandardMapper;
import com.example.storage.service.SalaryStandardService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 薪酬标准表 Service 实现类
 */
@Service
public class SalaryStandardServiceImpl extends ServiceImpl<SalaryStandardMapper, SalaryStandard> implements SalaryStandardService {

    @Override
    public SalaryStandard getByPositionIdAndJobTitle(Long positionId, String jobTitle) {
        LambdaQueryWrapper<SalaryStandard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryStandard::getPositionId, positionId)
               .eq(SalaryStandard::getJobTitle, jobTitle);
        return getOne(wrapper);
    }

    @Override
    public List<SalaryStandard> getByStatus(String status) {
        LambdaQueryWrapper<SalaryStandard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryStandard::getStatus, status);
        return list(wrapper);
    }

    @Override
    public List<SalaryStandard> getPendingReview() {
        return getByStatus("PENDING_REVIEW");
    }

    @Override
    public List<SalaryStandard> getApproved() {
        return getByStatus("APPROVED");
    }

    @Override
    public SalaryStandard getByStandardCode(String standardCode) {
        LambdaQueryWrapper<SalaryStandard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryStandard::getStandardCode, standardCode);
        return getOne(wrapper);
    }
}

