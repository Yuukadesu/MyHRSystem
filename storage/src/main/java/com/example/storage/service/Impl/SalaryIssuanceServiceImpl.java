package com.example.storage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.SalaryIssuance;
import com.example.storage.mapper.SalaryIssuanceMapper;
import com.example.storage.service.SalaryIssuanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 薪酬发放单表 Service 实现类
 */
@Service
public class SalaryIssuanceServiceImpl extends ServiceImpl<SalaryIssuanceMapper, SalaryIssuance> implements SalaryIssuanceService {

    @Override
    public SalaryIssuance getByThirdOrgIdAndMonth(Long thirdOrgId, LocalDate issuanceMonth) {
        LambdaQueryWrapper<SalaryIssuance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryIssuance::getThirdOrgId, thirdOrgId)
               .eq(SalaryIssuance::getIssuanceMonth, issuanceMonth);
        return getOne(wrapper);
    }

    @Override
    public List<SalaryIssuance> getByStatus(String status) {
        LambdaQueryWrapper<SalaryIssuance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryIssuance::getStatus, status);
        return list(wrapper);
    }

    @Override
    public List<SalaryIssuance> getPendingRegistration() {
        return getByStatus("PENDING_REGISTRATION");
    }

    @Override
    public List<SalaryIssuance> getPendingReview() {
        return getByStatus("PENDING_REVIEW");
    }

    @Override
    public List<SalaryIssuance> getExecuted() {
        return getByStatus("EXECUTED");
    }

    @Override
    public SalaryIssuance getBySalarySlipNumber(String salarySlipNumber) {
        LambdaQueryWrapper<SalaryIssuance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryIssuance::getSalarySlipNumber, salarySlipNumber);
        return getOne(wrapper);
    }
}

