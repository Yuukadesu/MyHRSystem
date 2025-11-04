# 人力资源管理系统数据库设计文档

## 1. 概述

本文档描述了人力资源管理系统的数据库设计，包括表结构、字段说明、关系设计等内容。

## 2. 数据库设计原则

- **数据完整性**：通过外键约束保证数据一致性
- **可追溯性**：记录创建时间、更新时间、操作人等审计信息
- **软删除**：员工档案删除采用状态标记，支持恢复
- **状态管理**：通过状态字段管理业务流程（待复核、已通过、已删除等）
- **编号规则**：系统自动生成唯一编号，支持业务规则

## 3. 核心实体关系图

```
用户(user)
  │
  ├──> 薪酬标准(salary_standard) [登记人、复核人、制定人]
  ├──> 员工档案(employee_archive) [登记人、复核人]
  └──> 薪酬发放单(salary_issuance) [登记人、复核人]

机构(organization) [自关联，三级层级]
  │
  ├──> 职位(position) [属于三级机构]
  ├──> 员工档案(employee_archive) [一级、二级、三级机构]
  └──> 薪酬发放单(salary_issuance) [三级机构]

职位(position)
  │
  ├──> 薪酬标准(salary_standard) [适用职位]
  └──> 员工档案(employee_archive) [职位]

薪酬项目(salary_item)
  │
  └──> 薪酬标准明细(salary_standard_item)

薪酬标准(salary_standard)
  │
  ├──> 薪酬标准明细(salary_standard_item)
  └──> 员工档案(employee_archive) [关联薪酬标准]

薪酬发放单(salary_issuance)
  │
  └──> 薪酬发放明细(salary_issuance_detail)

员工档案(employee_archive)
  │
  └──> 薪酬发放明细(salary_issuance_detail)
```

## 4. 数据表设计

### 4.1 用户表 (user)

**用途**：存储系统用户信息，包括人事专员、人事经理、薪酬专员、薪酬经理等角色。

**字段说明**：

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| user_id | BIGINT | 用户ID | PK, AUTO_INCREMENT |
| username | VARCHAR(50) | 用户名 | UNIQUE, NOT NULL |
| password | VARCHAR(255) | 密码（加密存储） | NOT NULL |
| real_name | VARCHAR(50) | 真实姓名 | NOT NULL |
| role | VARCHAR(20) | 角色 | NOT NULL |
| email | VARCHAR(100) | 邮箱 | |
| phone | VARCHAR(20) | 电话 | |
| status | VARCHAR(20) | 状态 | DEFAULT 'ACTIVE' |
| create_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| update_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

**角色枚举值**：
- HR_SPECIALIST：人事专员
- HR_MANAGER：人事经理
- SALARY_SPECIALIST：薪酬专员
- SALARY_MANAGER：薪酬经理

---

### 4.2 机构表 (organization)

**用途**：存储一级、二级、三级机构信息及其层级关系。

**字段说明**：

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| org_id | BIGINT | 机构ID | PK, AUTO_INCREMENT |
| org_code | VARCHAR(2) | 机构编号（用于生成档案编号） | NOT NULL |
| org_name | VARCHAR(100) | 机构名称 | NOT NULL |
| org_level | TINYINT | 机构级别：1(一级), 2(二级), 3(三级) | NOT NULL |
| parent_id | BIGINT | 父机构ID | FK -> organization(org_id) |
| description | VARCHAR(500) | 描述 | |
| status | VARCHAR(20) | 状态 | DEFAULT 'ACTIVE' |
| create_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| update_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

**层级关系**：
- 一级机构的 parent_id 为 NULL
- 二级机构的 parent_id 指向一级机构
- 三级机构的 parent_id 指向二级机构

**示例数据**：
```
一级机构：技术中心 (org_id=1, parent_id=NULL)
  └─ 二级机构：产品研发部 (org_id=3, parent_id=1)
      └─ 三级机构：前端组 (org_id=5, parent_id=3)
```

---

### 4.3 职位表 (position)

**用途**：存储职位信息，每个职位从属于一个三级机构。

**字段说明**：

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| position_id | BIGINT | 职位ID | PK, AUTO_INCREMENT |
| position_name | VARCHAR(100) | 职位名称 | NOT NULL |
| third_org_id | BIGINT | 所属三级机构ID | FK -> organization(org_id), NOT NULL |
| description | VARCHAR(500) | 职位描述 | |
| status | VARCHAR(20) | 状态 | DEFAULT 'ACTIVE' |
| create_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| update_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

---

### 4.4 薪酬项目表 (salary_item)

**用途**：存储薪酬项目配置，如基本工资、绩效奖金、各种补贴和扣除项。

**字段说明**：

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| item_id | BIGINT | 薪酬项目ID | PK, AUTO_INCREMENT |
| item_code | VARCHAR(20) | 项目编号（如：S001） | UNIQUE, NOT NULL |
| item_name | VARCHAR(100) | 项目名称 | NOT NULL |
| item_type | VARCHAR(20) | 项目类型：INCOME(收入), DEDUCTION(扣除) | NOT NULL |
| calculation_rule | VARCHAR(500) | 计算规则（如：基本工资*8%） | |
| sort_order | INT | 排序顺序 | DEFAULT 0 |
| status | VARCHAR(20) | 状态 | DEFAULT 'ACTIVE' |
| create_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| update_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

**计算规则说明**：
- 如果 calculation_rule 为空，表示需要手动输入金额
- 如果有计算规则（如"基本工资*8%"），系统自动计算，可在此基础上调整

**示例数据**：
- S001：基本工资（收入项，手动输入）
- S002：绩效奖金（收入项，手动输入）
- S006：养老保险（扣除项，计算规则：基本工资*8%）

---

### 4.5 薪酬标准表 (salary_standard)

**用途**：定义薪酬标准，包括适用职位、职称和对应的薪酬项目金额。

**字段说明**：

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| standard_id | BIGINT | 薪酬标准ID | PK, AUTO_INCREMENT |
| standard_code | VARCHAR(50) | 薪酬标准编号（如：SAL202307001） | UNIQUE, NOT NULL |
| standard_name | VARCHAR(200) | 薪酬标准名称（如：前端工程师-中级标准） | NOT NULL |
| position_id | BIGINT | 适用职位ID | FK -> position(position_id), NOT NULL |
| job_title | VARCHAR(20) | 职称：JUNIOR(初级), INTERMEDIATE(中级), SENIOR(高级) | NOT NULL |
| formulator_id | BIGINT | 制定人ID | FK -> user(user_id) |
| registrar_id | BIGINT | 登记人ID | FK -> user(user_id), NOT NULL |
| registration_time | DATETIME | 登记时间 | DEFAULT CURRENT_TIMESTAMP |
| reviewer_id | BIGINT | 复核人ID | FK -> user(user_id) |
| review_time | DATETIME | 复核时间 | |
| review_comments | TEXT | 复核意见（大段文本） | |
| status | VARCHAR(20) | 状态：PENDING_REVIEW(待复核), APPROVED(已通过), REJECTED(已驳回) | DEFAULT 'PENDING_REVIEW' |
| create_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| update_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

**业务流程**：
1. 薪酬专员/薪酬经理创建薪酬标准（status='PENDING_REVIEW'）
2. 薪酬经理复核（status='APPROVED'或'REJECTED'）
3. 只有状态为'APPROVED'的薪酬标准才能关联到员工档案

---

### 4.6 薪酬标准明细表 (salary_standard_item)

**用途**：存储薪酬标准包含的薪酬项目及其金额。

**字段说明**：

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| standard_item_id | BIGINT | 薪酬标准明细ID | PK, AUTO_INCREMENT |
| standard_id | BIGINT | 薪酬标准ID | FK -> salary_standard(standard_id), NOT NULL |
| item_id | BIGINT | 薪酬项目ID | FK -> salary_item(item_id), NOT NULL |
| amount | DECIMAL(10,2) | 金额（保留两位小数） | DEFAULT 0.00 |
| is_calculated | TINYINT(1) | 是否根据计算规则计算：0(手动), 1(自动) | DEFAULT 0 |
| create_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| update_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

**唯一约束**：每个薪酬标准中，每个薪酬项目只能出现一次（UNIQUE(standard_id, item_id)）

---

### 4.7 员工档案表 (employee_archive)

**用途**：存储员工的所有档案信息，这是系统的核心数据表。

**字段说明**：

#### 基本信息
| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| archive_id | BIGINT | 档案ID | PK, AUTO_INCREMENT |
| archive_number | VARCHAR(18) | 档案编号（唯一，系统生成） | UNIQUE, NOT NULL |
| name | VARCHAR(50) | 姓名 | NOT NULL |
| gender | VARCHAR(10) | 性别：MALE(男), FEMALE(女) | |
| id_number | VARCHAR(18) | 身份证号码 | |
| birthday | DATE | 出生日期 | |
| age | INT | 年龄 | |
| nationality | VARCHAR(50) | 国籍 | DEFAULT '中国' |
| place_of_birth | VARCHAR(100) | 出生地 | |
| ethnicity | VARCHAR(50) | 民族 | |
| religious_belief | VARCHAR(100) | 宗教信仰 | |
| political_status | VARCHAR(50) | 政治面貌 | |
| education_level | VARCHAR(50) | 学历 | |
| major | VARCHAR(100) | 学历专业 | |

#### 联系信息
| 字段名 | 类型 | 说明 |
|--------|------|------|
| email | VARCHAR(100) | Email |
| phone | VARCHAR(20) | 电话 |
| qq | VARCHAR(20) | QQ |
| mobile | VARCHAR(20) | 手机 |
| address | VARCHAR(500) | 住址 |
| postal_code | VARCHAR(10) | 邮编 |

#### 其他信息
| 字段名 | 类型 | 说明 |
|--------|------|------|
| hobby | TEXT | 爱好 |
| personal_resume | TEXT | 个人履历（大段文本） |
| family_relationship | TEXT | 家庭关系信息（大段文本） |
| remarks | TEXT | 备注（大段文本） |
| photo_url | VARCHAR(500) | 照片URL |

#### 机构职位信息
| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| first_org_id | BIGINT | 一级机构ID | FK -> organization(org_id), NOT NULL |
| second_org_id | BIGINT | 二级机构ID | FK -> organization(org_id), NOT NULL |
| third_org_id | BIGINT | 三级机构ID | FK -> organization(org_id), NOT NULL |
| position_id | BIGINT | 职位ID | FK -> position(position_id), NOT NULL |
| job_title | VARCHAR(20) | 职称：JUNIOR(初级), INTERMEDIATE(中级), SENIOR(高级) | NOT NULL |

#### 薪酬信息
| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| salary_standard_id | BIGINT | 薪酬标准ID | FK -> salary_standard(standard_id) |

#### 流程信息
| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| registrar_id | BIGINT | 登记人ID | FK -> user(user_id), NOT NULL |
| registration_time | DATETIME | 登记时间 | DEFAULT CURRENT_TIMESTAMP |
| reviewer_id | BIGINT | 复核人ID | FK -> user(user_id) |
| review_time | DATETIME | 复核时间 | |
| status | VARCHAR(20) | 状态：PENDING_REVIEW(待复核), NORMAL(正常), DELETED(已删除) | DEFAULT 'PENDING_REVIEW' |
| delete_time | DATETIME | 删除时间 | |
| delete_reason | VARCHAR(500) | 删除原因 | |
| create_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| update_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

**档案编号生成规则**：
- 格式：年份（4位）+ 一级机构编号（2位）+ 二级机构编号（2位）+ 三级机构编号（2位）+ 序号（2位）
- 示例：20230101010101（2023年 + 技术中心01 + 产品研发部01 + 前端组01 + 序号01）

**业务流程**：
1. 人事专员登记档案 → status='PENDING_REVIEW'
2. 人事经理复核 → status='NORMAL'
3. 人事经理删除 → status='DELETED'（软删除，可恢复）
4. 恢复删除 → status='NORMAL'

**重要约束**：
- 档案编号、所属机构、职位在复核后不能修改（在复核环节可以修改）
- 状态为'PENDING_REVIEW'的档案不能删除

---

### 4.8 薪酬发放单表 (salary_issuance)

**用途**：存储按机构按月度的薪酬发放单。

**字段说明**：

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| issuance_id | BIGINT | 薪酬发放单ID | PK, AUTO_INCREMENT |
| salary_slip_number | VARCHAR(50) | 薪酬单号（如：PAY202307001） | UNIQUE, NOT NULL |
| third_org_id | BIGINT | 三级机构ID | FK -> organization(org_id), NOT NULL |
| total_employees | INT | 总人数 | DEFAULT 0 |
| total_basic_salary | DECIMAL(12,2) | 基本薪酬总额 | DEFAULT 0.00 |
| total_net_pay | DECIMAL(12,2) | 实发薪酬总额 | DEFAULT 0.00 |
| issuance_month | DATE | 发放月份 | NOT NULL |
| registrar_id | BIGINT | 登记人ID | FK -> user(user_id), NOT NULL |
| registration_time | DATETIME | 登记时间 | DEFAULT CURRENT_TIMESTAMP |
| reviewer_id | BIGINT | 复核人ID | FK -> user(user_id) |
| review_time | DATETIME | 复核时间 | |
| status | VARCHAR(20) | 状态：PENDING_REGISTRATION(待登记), PENDING_REVIEW(待复核), EXECUTED(执行), PAID(已付款) | DEFAULT 'PENDING_REGISTRATION' |
| payment_status | VARCHAR(20) | 付款状态（由财务系统更新） | |
| create_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| update_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

**业务流程**：
1. 系统生成薪酬发放单（按三级机构+月份） → status='PENDING_REGISTRATION'
2. 薪酬专员登记详细薪酬 → status='PENDING_REVIEW'
3. 薪酬经理复核 → status='EXECUTED'
4. 财务系统付款 → payment_status='PAID'

---

### 4.9 薪酬发放明细表 (salary_issuance_detail)

**用途**：存储薪酬发放单中每个员工的详细薪酬构成。

**字段说明**：

#### 基本信息
| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| detail_id | BIGINT | 薪酬发放明细ID | PK, AUTO_INCREMENT |
| issuance_id | BIGINT | 薪酬发放单ID | FK -> salary_issuance(issuance_id), NOT NULL |
| employee_id | BIGINT | 员工档案ID | FK -> employee_archive(archive_id), NOT NULL |
| employee_number | VARCHAR(50) | 员工编号 | |
| employee_name | VARCHAR(50) | 员工姓名 | NOT NULL |
| position_name | VARCHAR(100) | 职位名称 | |

#### 薪酬明细（收入项）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| basic_salary | DECIMAL(10,2) | 基本工资 |
| performance_bonus | DECIMAL(10,2) | 绩效奖金 |
| transportation_allowance | DECIMAL(10,2) | 交通补贴 |
| meal_allowance | DECIMAL(10,2) | 餐费补贴 |

#### 薪酬明细（扣除项）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| pension_insurance | DECIMAL(10,2) | 养老保险 |
| medical_insurance | DECIMAL(10,2) | 医疗保险 |
| unemployment_insurance | DECIMAL(10,2) | 失业保险 |
| housing_fund | DECIMAL(10,2) | 住房公积金 |

#### 其他项
| 字段名 | 类型 | 说明 |
|--------|------|------|
| award_amount | DECIMAL(10,2) | 奖励金额（可编辑） |
| deduction_amount | DECIMAL(10,2) | 应扣金额（可编辑） |
| total_income | DECIMAL(10,2) | 总收入 |
| total_deduction | DECIMAL(10,2) | 总扣除 |
| net_pay | DECIMAL(10,2) | 实发金额 |

#### 时间戳
| 字段名 | 类型 | 说明 |
|--------|------|------|
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

**计算逻辑**：
- 根据员工的薪酬标准，自动填充各项薪酬明细
- 三险一金根据基本工资自动计算：
  - 养老保险 = 基本工资 * 8%
  - 医疗保险 = 基本工资 * 2% + 3元
  - 失业保险 = 基本工资 * 0.5%
  - 住房公积金 = 基本工资 * 8%
- 总收入 = 所有收入项之和 + 奖励金额
- 总扣除 = 所有扣除项之和 + 应扣金额
- 实发金额 = 总收入 - 总扣除

---

## 5. 索引设计

### 5.1 主要索引

#### 用户表
- `idx_username`：用户名索引（已包含在UNIQUE约束中）

#### 机构表
- `idx_parent_id`：父机构ID索引（用于查询子机构）
- `idx_org_level`：机构级别索引（用于查询特定级别的机构）

#### 职位表
- `idx_third_org_id`：三级机构ID索引（用于查询某机构的职位）

#### 薪酬标准表
- `idx_standard_code`：薪酬标准编号索引
- `idx_position_id`：职位ID索引
- `idx_status`：状态索引（用于查询待复核的薪酬标准）

#### 员工档案表
- `idx_archive_number`：档案编号索引（已包含在UNIQUE约束中）
- `idx_name`：姓名索引（用于查询）
- `idx_first_org_id`、`idx_second_org_id`、`idx_third_org_id`：机构ID索引（用于查询）
- `idx_position_id`：职位ID索引
- `idx_status`：状态索引
- `idx_registration_time`：登记时间索引（用于时间范围查询）

#### 薪酬发放单表
- `idx_salary_slip_number`：薪酬单号索引（已包含在UNIQUE约束中）
- `idx_third_org_id`：三级机构ID索引
- `idx_status`：状态索引
- `idx_issuance_month`：发放月份索引

#### 薪酬发放明细表
- `idx_issuance_id`：薪酬发放单ID索引
- `idx_employee_id`：员工ID索引

---

## 6. 业务流程说明

### 6.1 员工档案管理流程

```
1. 人事专员登记档案
   - 填写员工基本信息
   - 选择机构（一级、二级、三级）
   - 选择职位
   - 选择职称
   - 选择薪酬标准（根据职位和职称筛选）
   - 上传照片
   - 提交 → status='PENDING_REVIEW'

2. 人事经理复核档案
   - 查看待复核档案列表
   - 打开档案详情
   - 可以修改除档案编号、机构、职位外的所有信息
   - 通过复核 → status='NORMAL'
   - 或驳回（可填写复核意见）

3. 档案变更
   - 人事专员查询档案
   - 选择要变更的档案（status必须为'NORMAL'）
   - 修改信息（机构、职位不能修改）
   - 提交 → status='PENDING_REVIEW'
   - 等待人事经理复核

4. 档案删除
   - 人事经理查询档案
   - 选择要删除的档案（status必须为'NORMAL'，不能是'PENDING_REVIEW'）
   - 填写删除原因
   - 确认删除 → status='DELETED', delete_time=CURRENT_TIMESTAMP
   - 已删除的档案可以恢复 → status='NORMAL', delete_time=NULL, delete_reason=NULL
```

### 6.2 薪酬管理流程

```
1. 设置薪酬项目（系统管理模块）
   - 添加/删除薪酬项目
   - 设置计算规则（如养老保险=基本工资*8%）

2. 创建薪酬标准
   - 薪酬专员/薪酬经理创建薪酬标准
   - 选择适用职位
   - 选择职称
   - 选择薪酬项目
   - 填写各项目金额
   - 提交 → status='PENDING_REVIEW'

3. 复核薪酬标准
   - 薪酬经理查看待复核列表
   - 复核薪酬标准
   - 可以修改金额
   - 填写复核意见
   - 通过 → status='APPROVED'

4. 设置员工薪酬标准
   - 在员工档案登记时，根据职位和职称自动匹配薪酬标准
   - 或在档案变更时调整薪酬标准
   - 复核后生效

5. 薪酬发放登记
   - 系统按三级机构+月份生成薪酬发放单
   - 薪酬专员登记具体薪酬明细
   - 系统根据员工薪酬标准自动填充
   - 可以调整奖励金额、应扣金额
   - 提交 → status='PENDING_REVIEW'

6. 薪酬发放复核
   - 薪酬经理查看待复核薪酬单
   - 可以修改奖励金额、应扣金额
   - 通过复核 → status='EXECUTED'

7. 财务系统付款
   - 财务系统查询状态为'EXECUTED'的薪酬单
   - 完成付款后更新 payment_status='PAID'
```

---

## 7. 数据约束说明

### 7.1 外键约束

- 所有外键都设置了适当的删除策略：
  - `ON DELETE RESTRICT`：阻止删除被引用的记录（如机构、职位）
  - `ON DELETE CASCADE`：级联删除（如薪酬标准明细随薪酬标准删除）
  - `ON DELETE SET NULL`：设置为NULL（如复核人删除后设置为NULL）

### 7.2 唯一约束

- `user.username`：用户名唯一
- `organization.org_code`：机构编号在相同级别下应该唯一（需要在应用层控制）
- `salary_item.item_code`：薪酬项目编号唯一
- `salary_standard.standard_code`：薪酬标准编号唯一
- `employee_archive.archive_number`：档案编号唯一
- `salary_issuance.salary_slip_number`：薪酬单号唯一
- `salary_standard_item(standard_id, item_id)`：薪酬标准中每个项目只能出现一次

### 7.3 状态约束（应用层控制）

- 只有状态为'APPROVED'的薪酬标准才能关联到员工档案
- 只有状态为'NORMAL'的档案才能进行变更和删除
- 只有状态为'PENDING_REVIEW'的档案才能进行复核
- 只有状态为'EXECUTED'的薪酬单才能被财务系统处理

---

## 8. 数据字典

### 8.1 状态枚举

#### 用户状态 (user.status)
- ACTIVE：激活
- INACTIVE：禁用

#### 机构/职位状态 (organization.status, position.status)
- ACTIVE：激活
- INACTIVE：禁用

#### 薪酬标准状态 (salary_standard.status)
- PENDING_REVIEW：待复核
- APPROVED：已通过
- REJECTED：已驳回

#### 员工档案状态 (employee_archive.status)
- PENDING_REVIEW：待复核
- NORMAL：正常
- DELETED：已删除

#### 薪酬发放单状态 (salary_issuance.status)
- PENDING_REGISTRATION：待登记
- PENDING_REVIEW：待复核
- EXECUTED：执行（已复核通过）
- PAID：已付款

#### 付款状态 (salary_issuance.payment_status)
- PENDING：待付款
- PAID：已付款

### 8.2 角色枚举 (user.role)
- HR_SPECIALIST：人事专员
- HR_MANAGER：人事经理
- SALARY_SPECIALIST：薪酬专员
- SALARY_MANAGER：薪酬经理

### 8.3 性别枚举 (employee_archive.gender)
- MALE：男
- FEMALE：女

### 8.4 职称枚举 (salary_standard.job_title, employee_archive.job_title)
- JUNIOR：初级
- INTERMEDIATE：中级
- SENIOR：高级

### 8.5 薪酬项目类型 (salary_item.item_type)
- INCOME：收入项
- DEDUCTION：扣除项

---

## 9. 编号生成规则

### 9.1 档案编号 (archive_number)
- 格式：YYYY + O1O2O3 + SS
- YYYY：4位年份
- O1O2O3：一级机构编号（2位）+ 二级机构编号（2位）+ 三级机构编号（2位）
- SS：序号（2位，同一机构下递增）
- 示例：20230101010101

### 9.2 薪酬标准编号 (standard_code)
- 格式：SAL + YYYYMM + NNN
- SAL：固定前缀
- YYYYMM：6位年月
- NNN：3位序号
- 示例：SAL202307001

### 9.3 薪酬单号 (salary_slip_number)
- 格式：PAY + YYYYMM + NNN
- PAY：固定前缀
- YYYYMM：6位年月
- NNN：3位序号
- 示例：PAY202307001

---

## 10. 注意事项

1. **机构编号唯一性**：同一个父机构下，机构编号应该唯一，需要在应用层控制
2. **薪酬计算**：三险一金等扣除项的计算规则存储在`salary_item.calculation_rule`中，需要在应用层解析并计算
3. **照片存储**：员工照片建议存储在文件服务器或对象存储中，数据库只存储URL
4. **大文本字段**：个人履历、家庭关系信息、备注等使用TEXT类型，支持大段文本
5. **软删除**：员工档案删除采用软删除机制，通过状态字段控制，支持恢复
6. **审计追踪**：所有表都包含创建时间、更新时间，关键操作记录操作人ID和时间
7. **数据精度**：金额字段使用DECIMAL(10,2)或DECIMAL(12,2)，保留两位小数

---

## 11. 后续扩展建议

1. **权限管理**：可以增加权限表，实现更细粒度的权限控制
2. **操作日志**：可以增加操作日志表，记录所有关键操作
3. **消息通知**：可以增加消息通知表，实现复核提醒等功能
4. **数据备份**：建议定期备份数据库，特别是员工档案数据
5. **数据归档**：对于历史数据，可以考虑归档机制

