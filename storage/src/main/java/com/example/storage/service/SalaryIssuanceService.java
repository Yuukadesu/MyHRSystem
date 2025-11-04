package com.example.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.SalaryIssuance;

import java.time.LocalDate;
import java.util.List;

/**
 * 薪酬发放单表 Service 接口
 */
public interface SalaryIssuanceService extends IService<SalaryIssuance> {
    /**
     * 根据三级机构ID和发放月份查询薪酬发放单
     *
     * @param thirdOrgId   三级机构ID
     * @param issuanceMonth 发放月份
     * @return 薪酬发放单
     */
    SalaryIssuance getByThirdOrgIdAndMonth(Long thirdOrgId, LocalDate issuanceMonth);

    /**
     * 根据状态查询薪酬发放单列表
     *
     * @param status 状态：PENDING_REGISTRATION(待登记), PENDING_REVIEW(待复核), EXECUTED(执行), PAID(已付款)
     * @return 薪酬发放单列表
     */
    List<SalaryIssuance> getByStatus(String status);

    /**
     * 获取待登记的薪酬发放单列表
     *
     * @return 待登记的薪酬发放单列表
     */
    List<SalaryIssuance> getPendingRegistration();

    /**
     * 获取待复核的薪酬发放单列表
     *
     * @return 待复核的薪酬发放单列表
     */
    List<SalaryIssuance> getPendingReview();

    /**
     * 获取已执行的薪酬发放单列表
     *
     * @return 已执行的薪酬发放单列表
     */
    List<SalaryIssuance> getExecuted();

    /**
     * 根据薪酬单号查询
     *
     * @param salarySlipNumber 薪酬单号
     * @return 薪酬发放单
     */
    SalaryIssuance getBySalarySlipNumber(String salarySlipNumber);
}

