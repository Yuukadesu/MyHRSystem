package com.example.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.SalaryIssuanceDetail;

import java.util.List;

/**
 * 薪酬发放明细表 Service 接口
 */
public interface SalaryIssuanceDetailService extends IService<SalaryIssuanceDetail> {
    /**
     * 根据薪酬发放单ID查询薪酬发放明细列表
     *
     * @param issuanceId 薪酬发放单ID
     * @return 薪酬发放明细列表
     */
    List<SalaryIssuanceDetail> getByIssuanceId(Long issuanceId);

    /**
     * 根据员工ID查询薪酬发放明细列表
     *
     * @param employeeId 员工ID
     * @return 薪酬发放明细列表
     */
    List<SalaryIssuanceDetail> getByEmployeeId(Long employeeId);

    /**
     * 根据薪酬发放单ID删除所有明细
     *
     * @param issuanceId 薪酬发放单ID
     * @return 是否删除成功
     */
    boolean deleteByIssuanceId(Long issuanceId);
}

