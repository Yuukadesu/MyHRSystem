# 数据库设计文件说明

## 文件列表

1. **myHRSystem.sql** - 数据库建表脚本
   - 包含所有表的CREATE TABLE语句
   - 包含外键约束和索引
   - 包含初始化示例数据

2. **DATABASE_DESIGN.md** - 详细数据库设计文档
   - 完整的表结构说明
   - 字段详细描述
   - 业务流程说明
   - 数据字典

## 快速开始

### 1. 创建数据库

```sql
CREATE DATABASE hr_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hr_system;
```

### 2. 执行建表脚本

```bash
mysql -u root -p hr_system < myHRSystem.sql
```

或者在MySQL客户端中直接执行：
```sql
SOURCE /path/to/myHRSystem.sql;
```

## 核心表关系

### 三级机构层级关系
```
organization (一级机构)
  └── organization (二级机构)
      └── organization (三级机构)
          └── position (职位)
```

### 薪酬标准体系
```
salary_item (薪酬项目)
  └── salary_standard_item (薪酬标准明细)
      └── salary_standard (薪酬标准)
          └── employee_archive (员工档案)
```

### 薪酬发放流程
```
organization (三级机构)
  └── salary_issuance (薪酬发放单)
      └── salary_issuance_detail (薪酬发放明细)
          └── employee_archive (员工档案)
```

## 主要业务流程

### 员工档案管理
1. 人事专员登记 → 状态：待复核
2. 人事经理复核 → 状态：正常
3. 档案变更 → 状态：待复核（需再次复核）
4. 档案删除 → 状态：已删除（软删除，可恢复）

### 薪酬管理
1. 设置薪酬项目
2. 创建薪酬标准 → 状态：待复核
3. 复核薪酬标准 → 状态：已通过
4. 关联员工薪酬标准
5. 薪酬发放登记 → 状态：待复核
6. 薪酬发放复核 → 状态：执行
7. 财务系统付款 → 付款状态：已付款

## 注意事项

1. 所有金额字段使用 DECIMAL 类型，保留两位小数
2. 时间字段使用 DATETIME 类型
3. 大文本字段使用 TEXT 类型
4. 员工档案采用软删除机制（通过status字段控制）
5. 所有表都包含 create_time 和 update_time 字段用于审计

## 数据字典

详细的数据字典和枚举值请参考 `DATABASE_DESIGN.md` 文档。

