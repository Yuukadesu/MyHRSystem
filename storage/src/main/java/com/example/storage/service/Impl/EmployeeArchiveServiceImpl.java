package com.example.storage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.EmployeeArchive;
import com.example.storage.mapper.EmployeeArchiveMapper;
import com.example.storage.service.EmployeeArchiveService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工档案表 Service 实现类
 */
@Service
public class EmployeeArchiveServiceImpl extends ServiceImpl<EmployeeArchiveMapper, EmployeeArchive> implements EmployeeArchiveService {

    @Override
    public List<EmployeeArchive> getByThirdOrgId(Long thirdOrgId) {
        LambdaQueryWrapper<EmployeeArchive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeArchive::getThirdOrgId, thirdOrgId);
        return list(wrapper);
    }

    @Override
    public List<EmployeeArchive> getByStatus(String status) {
        LambdaQueryWrapper<EmployeeArchive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeArchive::getStatus, status);
        return list(wrapper);
    }

    @Override
    public List<EmployeeArchive> getByPositionId(Long positionId) {
        LambdaQueryWrapper<EmployeeArchive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeArchive::getPositionId, positionId);
        return list(wrapper);
    }

    @Override
    public List<EmployeeArchive> getPendingReview() {
        return getByStatus("PENDING_REVIEW");
    }

    @Override
    public List<EmployeeArchive> getNormal() {
        return getByStatus("NORMAL");
    }

    @Override
    public List<EmployeeArchive> getDeleted() {
        return getByStatus("DELETED");
    }

    @Override
    public EmployeeArchive getByArchiveNumber(String archiveNumber) {
        LambdaQueryWrapper<EmployeeArchive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeArchive::getArchiveNumber, archiveNumber);
        return getOne(wrapper);
    }

    @Override
    public List<EmployeeArchive> getByRegistrationTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<EmployeeArchive> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(EmployeeArchive::getRegistrationTime, startTime)
               .le(EmployeeArchive::getRegistrationTime, endTime);
        return list(wrapper);
    }

    @Override
    public boolean softDelete(Long archiveId, String deleteReason) {
        LambdaUpdateWrapper<EmployeeArchive> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(EmployeeArchive::getArchiveId, archiveId)
               .set(EmployeeArchive::getStatus, "DELETED")
               .set(EmployeeArchive::getDeleteTime, LocalDateTime.now())
               .set(EmployeeArchive::getDeleteReason, deleteReason);
        return update(wrapper);
    }

    @Override
    public boolean restore(Long archiveId) {
        LambdaUpdateWrapper<EmployeeArchive> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(EmployeeArchive::getArchiveId, archiveId)
               .set(EmployeeArchive::getStatus, "NORMAL")
               .set(EmployeeArchive::getDeleteTime, null)
               .set(EmployeeArchive::getDeleteReason, null);
        return update(wrapper);
    }
}

