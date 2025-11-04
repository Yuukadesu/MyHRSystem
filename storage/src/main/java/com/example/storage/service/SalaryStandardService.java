package com.example.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.SalaryStandard;

import java.util.List;

/**
 * 薪酬标准表 Service 接口
 */
public interface SalaryStandardService extends IService<SalaryStandard> {
    /**
     * 根据职位ID和职称查询薪酬标准
     *
     * @param positionId 职位ID
     * @param jobTitle   职称：JUNIOR(初级), INTERMEDIATE(中级), SENIOR(高级)
     * @return 薪酬标准
     */
    SalaryStandard getByPositionIdAndJobTitle(Long positionId, String jobTitle);

    /**
     * 根据状态查询薪酬标准列表
     *
     * @param status 状态：PENDING_REVIEW(待复核), APPROVED(已通过), REJECTED(已驳回)
     * @return 薪酬标准列表
     */
    List<SalaryStandard> getByStatus(String status);

    /**
     * 获取待复核的薪酬标准列表
     *
     * @return 待复核的薪酬标准列表
     */
    List<SalaryStandard> getPendingReview();

    /**
     * 获取已通过的薪酬标准列表
     *
     * @return 已通过的薪酬标准列表
     */
    List<SalaryStandard> getApproved();

    /**
     * 根据薪酬标准编号查询
     *
     * @param standardCode 薪酬标准编号
     * @return 薪酬标准
     */
    SalaryStandard getByStandardCode(String standardCode);
}

