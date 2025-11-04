package com.example.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.SalaryStandardItem;

import java.util.List;

/**
 * 薪酬标准明细表 Service 接口
 */
public interface SalaryStandardItemService extends IService<SalaryStandardItem> {
    /**
     * 根据薪酬标准ID查询薪酬标准明细列表
     *
     * @param standardId 薪酬标准ID
     * @return 薪酬标准明细列表
     */
    List<SalaryStandardItem> getByStandardId(Long standardId);

    /**
     * 根据薪酬标准ID删除所有明细
     *
     * @param standardId 薪酬标准ID
     * @return 是否删除成功
     */
    boolean deleteByStandardId(Long standardId);
}

