package com.example.storage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.SalaryIssuanceDetail;
import com.example.storage.mapper.SalaryIssuanceDetailMapper;
import com.example.storage.service.SalaryIssuanceDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 薪酬发放明细表 Service 实现类
 */
@Service
public class SalaryIssuanceDetailServiceImpl extends ServiceImpl<SalaryIssuanceDetailMapper, SalaryIssuanceDetail> implements SalaryIssuanceDetailService {

    @Override
    public List<SalaryIssuanceDetail> getByIssuanceId(Long issuanceId) {
        LambdaQueryWrapper<SalaryIssuanceDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryIssuanceDetail::getIssuanceId, issuanceId);
        return list(wrapper);
    }

    @Override
    public List<SalaryIssuanceDetail> getByEmployeeId(Long employeeId) {
        LambdaQueryWrapper<SalaryIssuanceDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryIssuanceDetail::getEmployeeId, employeeId);
        return list(wrapper);
    }

    @Override
    public boolean deleteByIssuanceId(Long issuanceId) {
        LambdaQueryWrapper<SalaryIssuanceDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryIssuanceDetail::getIssuanceId, issuanceId);
        return remove(wrapper);
    }
}

