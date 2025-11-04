package com.example.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.EmployeeArchive;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工档案表 Service 接口
 */
public interface EmployeeArchiveService extends IService<EmployeeArchive> {
    /**
     * 根据三级机构ID查询员工档案列表
     *
     * @param thirdOrgId 三级机构ID
     * @return 员工档案列表
     */
    List<EmployeeArchive> getByThirdOrgId(Long thirdOrgId);

    /**
     * 根据状态查询员工档案列表
     *
     * @param status 状态：PENDING_REVIEW(待复核), NORMAL(正常), DELETED(已删除)
     * @return 员工档案列表
     */
    List<EmployeeArchive> getByStatus(String status);

    /**
     * 根据职位ID查询员工档案列表
     *
     * @param positionId 职位ID
     * @return 员工档案列表
     */
    List<EmployeeArchive> getByPositionId(Long positionId);

    /**
     * 获取待复核的员工档案列表
     *
     * @return 待复核的员工档案列表
     */
    List<EmployeeArchive> getPendingReview();

    /**
     * 获取正常的员工档案列表
     *
     * @return 正常的员工档案列表
     */
    List<EmployeeArchive> getNormal();

    /**
     * 获取已删除的员工档案列表
     *
     * @return 已删除的员工档案列表
     */
    List<EmployeeArchive> getDeleted();

    /**
     * 根据档案编号查询
     *
     * @param archiveNumber 档案编号
     * @return 员工档案
     */
    EmployeeArchive getByArchiveNumber(String archiveNumber);

    /**
     * 根据登记时间范围查询
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 员工档案列表
     */
    List<EmployeeArchive> getByRegistrationTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 软删除员工档案
     *
     * @param archiveId   档案ID
     * @param deleteReason 删除原因
     * @return 是否删除成功
     */
    boolean softDelete(Long archiveId, String deleteReason);

    /**
     * 恢复已删除的员工档案
     *
     * @param archiveId 档案ID
     * @return 是否恢复成功
     */
    boolean restore(Long archiveId);
}

