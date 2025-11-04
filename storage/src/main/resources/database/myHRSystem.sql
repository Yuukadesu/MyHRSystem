-- ============================================
-- 人力资源管理系统数据库设计
-- ============================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS myHRSystem DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE myHRSystem;

-- 临时禁用外键检查，以便删除表
SET FOREIGN_KEY_CHECKS = 0;

-- 删除已存在的表（按依赖顺序）
DROP TABLE IF EXISTS salary_issuance_detail;
DROP TABLE IF EXISTS salary_issuance;
DROP TABLE IF EXISTS employee_archive;
DROP TABLE IF EXISTS salary_standard_item;
DROP TABLE IF EXISTS salary_standard;
DROP TABLE IF EXISTS `position`;
DROP TABLE IF EXISTS organization;
DROP TABLE IF EXISTS salary_item;
DROP TABLE IF EXISTS `user`;

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 1. 用户表
-- ============================================
CREATE TABLE `user` (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    role VARCHAR(20) NOT NULL COMMENT '角色：HR_SPECIALIST(人事专员), HR_MANAGER(人事经理), SALARY_SPECIALIST(薪酬专员), SALARY_MANAGER(薪酬经理)',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE(激活), INACTIVE(禁用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 机构表（支持三级机构层级关系）
-- ============================================
CREATE TABLE organization (
    org_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '机构ID',
    org_code VARCHAR(2) NOT NULL COMMENT '机构编号（用于生成档案编号）',
    org_name VARCHAR(100) NOT NULL COMMENT '机构名称',
    org_level TINYINT NOT NULL COMMENT '机构级别：1(一级机构), 2(二级机构), 3(三级机构)',
    parent_id BIGINT NULL COMMENT '父机构ID（一级机构的parent_id为NULL）',
    description VARCHAR(500) COMMENT '描述',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE(激活), INACTIVE(禁用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (parent_id) REFERENCES organization(org_id) ON DELETE RESTRICT,
    INDEX idx_parent_id (parent_id),
    INDEX idx_org_level (org_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

-- ============================================
-- 3. 职位表
-- ============================================
CREATE TABLE `position` (
    position_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '职位ID',
    position_name VARCHAR(100) NOT NULL COMMENT '职位名称',
    third_org_id BIGINT NOT NULL COMMENT '所属三级机构ID',
    description VARCHAR(500) COMMENT '职位描述',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE(激活), INACTIVE(禁用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (third_org_id) REFERENCES organization(org_id) ON DELETE RESTRICT,
    INDEX idx_third_org_id (third_org_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';

-- ============================================
-- 4. 薪酬项目表
-- ============================================
CREATE TABLE salary_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '薪酬项目ID',
    item_code VARCHAR(20) NOT NULL UNIQUE COMMENT '项目编号（如：S001）',
    item_name VARCHAR(100) NOT NULL COMMENT '项目名称（如：基本工资、绩效奖金等）',
    item_type VARCHAR(20) NOT NULL COMMENT '项目类型：INCOME(收入项), DEDUCTION(扣除项)',
    calculation_rule VARCHAR(500) COMMENT '计算规则（如：基本工资*8%），为空表示手动输入',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE(激活), INACTIVE(禁用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_item_code (item_code),
    INDEX idx_item_type (item_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪酬项目表';

-- ============================================
-- 5. 薪酬标准表
-- ============================================
CREATE TABLE salary_standard (
    standard_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '薪酬标准ID',
    standard_code VARCHAR(50) NOT NULL UNIQUE COMMENT '薪酬标准编号（如：SAL202307001）',
    standard_name VARCHAR(200) NOT NULL COMMENT '薪酬标准名称（如：前端工程师-中级标准）',
    position_id BIGINT NOT NULL COMMENT '适用职位ID',
    job_title VARCHAR(20) NOT NULL COMMENT '职称：JUNIOR(初级), INTERMEDIATE(中级), SENIOR(高级)',
    formulator_id BIGINT COMMENT '制定人ID',
    registrar_id BIGINT NOT NULL COMMENT '登记人ID',
    registration_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登记时间',
    reviewer_id BIGINT COMMENT '复核人ID',
    review_time DATETIME COMMENT '复核时间',
    review_comments TEXT COMMENT '复核意见（大段文本）',
    status VARCHAR(20) DEFAULT 'PENDING_REVIEW' COMMENT '状态：PENDING_REVIEW(待复核), APPROVED(已通过), REJECTED(已驳回)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (position_id) REFERENCES `position`(position_id) ON DELETE RESTRICT,
    FOREIGN KEY (formulator_id) REFERENCES `user`(user_id) ON DELETE SET NULL,
    FOREIGN KEY (registrar_id) REFERENCES `user`(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (reviewer_id) REFERENCES `user`(user_id) ON DELETE SET NULL,
    INDEX idx_standard_code (standard_code),
    INDEX idx_position_id (position_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪酬标准表';

-- ============================================
-- 6. 薪酬标准明细表（薪酬标准包含的薪酬项目及金额）
-- ============================================
CREATE TABLE salary_standard_item (
    standard_item_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '薪酬标准明细ID',
    standard_id BIGINT NOT NULL COMMENT '薪酬标准ID',
    item_id BIGINT NOT NULL COMMENT '薪酬项目ID',
    amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '金额（保留两位小数）',
    is_calculated TINYINT(1) DEFAULT 0 COMMENT '是否根据计算规则计算：0(手动输入), 1(自动计算)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (standard_id) REFERENCES salary_standard(standard_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES salary_item(item_id) ON DELETE RESTRICT,
    UNIQUE KEY uk_standard_item (standard_id, item_id),
    INDEX idx_standard_id (standard_id),
    INDEX idx_item_id (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪酬标准明细表';

-- ============================================
-- 7. 员工档案表
-- ============================================
CREATE TABLE employee_archive (
    archive_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    archive_number VARCHAR(18) NOT NULL UNIQUE COMMENT '档案编号（年份4位+一级机构2位+二级机构2位+三级机构2位+编号2位）',
    -- 基本信息
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别：MALE(男), FEMALE(女)',
    id_number VARCHAR(18) COMMENT '身份证号码',
    birthday DATE COMMENT '出生日期',
    age INT COMMENT '年龄',
    nationality VARCHAR(50) DEFAULT '中国' COMMENT '国籍',
    place_of_birth VARCHAR(100) COMMENT '出生地',
    ethnicity VARCHAR(50) COMMENT '民族',
    religious_belief VARCHAR(100) COMMENT '宗教信仰',
    political_status VARCHAR(50) COMMENT '政治面貌',
    education_level VARCHAR(50) COMMENT '学历',
    major VARCHAR(100) COMMENT '学历专业',
    -- 联系信息
    email VARCHAR(100) COMMENT 'Email',
    phone VARCHAR(20) COMMENT '电话',
    qq VARCHAR(20) COMMENT 'QQ',
    mobile VARCHAR(20) COMMENT '手机',
    address VARCHAR(500) COMMENT '住址',
    postal_code VARCHAR(10) COMMENT '邮编',
    -- 其他信息
    hobby TEXT COMMENT '爱好',
    personal_resume TEXT COMMENT '个人履历（大段文本）',
    family_relationship TEXT COMMENT '家庭关系信息（大段文本）',
    remarks TEXT COMMENT '备注（大段文本）',
    photo_url VARCHAR(500) COMMENT '照片URL',
    -- 机构职位信息
    first_org_id BIGINT NOT NULL COMMENT '一级机构ID',
    second_org_id BIGINT NOT NULL COMMENT '二级机构ID',
    third_org_id BIGINT NOT NULL COMMENT '三级机构ID',
    position_id BIGINT NOT NULL COMMENT '职位ID',
    job_title VARCHAR(20) NOT NULL COMMENT '职称：JUNIOR(初级), INTERMEDIATE(中级), SENIOR(高级)',
    -- 薪酬信息
    salary_standard_id BIGINT COMMENT '薪酬标准ID',
    -- 流程信息
    registrar_id BIGINT NOT NULL COMMENT '登记人ID',
    registration_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登记时间',
    reviewer_id BIGINT COMMENT '复核人ID',
    review_time DATETIME COMMENT '复核时间',
    status VARCHAR(20) DEFAULT 'PENDING_REVIEW' COMMENT '状态：PENDING_REVIEW(待复核), NORMAL(正常), DELETED(已删除)',
    delete_time DATETIME COMMENT '删除时间',
    delete_reason VARCHAR(500) COMMENT '删除原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (first_org_id) REFERENCES organization(org_id) ON DELETE RESTRICT,
    FOREIGN KEY (second_org_id) REFERENCES organization(org_id) ON DELETE RESTRICT,
    FOREIGN KEY (third_org_id) REFERENCES organization(org_id) ON DELETE RESTRICT,
    FOREIGN KEY (position_id) REFERENCES `position`(position_id) ON DELETE RESTRICT,
    FOREIGN KEY (salary_standard_id) REFERENCES salary_standard(standard_id) ON DELETE SET NULL,
    FOREIGN KEY (registrar_id) REFERENCES `user`(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (reviewer_id) REFERENCES `user`(user_id) ON DELETE SET NULL,
    INDEX idx_archive_number (archive_number),
    INDEX idx_name (name),
    INDEX idx_first_org_id (first_org_id),
    INDEX idx_second_org_id (second_org_id),
    INDEX idx_third_org_id (third_org_id),
    INDEX idx_position_id (position_id),
    INDEX idx_status (status),
    INDEX idx_registration_time (registration_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工档案表';

-- ============================================
-- 8. 薪酬发放单表
-- ============================================
CREATE TABLE salary_issuance (
    issuance_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '薪酬发放单ID',
    salary_slip_number VARCHAR(50) NOT NULL UNIQUE COMMENT '薪酬单号（如：PAY202307001）',
    third_org_id BIGINT NOT NULL COMMENT '三级机构ID',
    total_employees INT DEFAULT 0 COMMENT '总人数',
    total_basic_salary DECIMAL(12,2) DEFAULT 0.00 COMMENT '基本薪酬总额',
    total_net_pay DECIMAL(12,2) DEFAULT 0.00 COMMENT '实发薪酬总额',
    issuance_month DATE NOT NULL COMMENT '发放月份',
    registrar_id BIGINT NOT NULL COMMENT '登记人ID',
    registration_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登记时间',
    reviewer_id BIGINT COMMENT '复核人ID',
    review_time DATETIME COMMENT '复核时间',
    status VARCHAR(20) DEFAULT 'PENDING_REGISTRATION' COMMENT '状态：PENDING_REGISTRATION(待登记), PENDING_REVIEW(待复核), EXECUTED(执行), PAID(已付款)',
    payment_status VARCHAR(20) COMMENT '付款状态（由财务系统更新）：PENDING(待付款), PAID(已付款)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (third_org_id) REFERENCES organization(org_id) ON DELETE RESTRICT,
    FOREIGN KEY (registrar_id) REFERENCES `user`(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (reviewer_id) REFERENCES `user`(user_id) ON DELETE SET NULL,
    INDEX idx_salary_slip_number (salary_slip_number),
    INDEX idx_third_org_id (third_org_id),
    INDEX idx_status (status),
    INDEX idx_issuance_month (issuance_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪酬发放单表';

-- ============================================
-- 9. 薪酬发放明细表（每个员工的薪酬明细）
-- ============================================
CREATE TABLE salary_issuance_detail (
    detail_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '薪酬发放明细ID',
    issuance_id BIGINT NOT NULL COMMENT '薪酬发放单ID',
    employee_id BIGINT NOT NULL COMMENT '员工档案ID',
    employee_number VARCHAR(50) COMMENT '员工编号',
    employee_name VARCHAR(50) NOT NULL COMMENT '员工姓名',
    position_name VARCHAR(100) COMMENT '职位名称',
    -- 各项薪酬明细（根据薪酬项目动态存储）
    basic_salary DECIMAL(10,2) DEFAULT 0.00 COMMENT '基本工资',
    performance_bonus DECIMAL(10,2) DEFAULT 0.00 COMMENT '绩效奖金',
    transportation_allowance DECIMAL(10,2) DEFAULT 0.00 COMMENT '交通补贴',
    meal_allowance DECIMAL(10,2) DEFAULT 0.00 COMMENT '餐费补贴',
    pension_insurance DECIMAL(10,2) DEFAULT 0.00 COMMENT '养老保险',
    medical_insurance DECIMAL(10,2) DEFAULT 0.00 COMMENT '医疗保险',
    unemployment_insurance DECIMAL(10,2) DEFAULT 0.00 COMMENT '失业保险',
    housing_fund DECIMAL(10,2) DEFAULT 0.00 COMMENT '住房公积金',
    -- 其他可调整项
    award_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '奖励金额',
    deduction_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '应扣金额',
    -- 合计
    total_income DECIMAL(10,2) DEFAULT 0.00 COMMENT '总收入',
    total_deduction DECIMAL(10,2) DEFAULT 0.00 COMMENT '总扣除',
    net_pay DECIMAL(10,2) DEFAULT 0.00 COMMENT '实发金额',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (issuance_id) REFERENCES salary_issuance(issuance_id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employee_archive(archive_id) ON DELETE RESTRICT,
    INDEX idx_issuance_id (issuance_id),
    INDEX idx_employee_id (employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪酬发放明细表';

-- ============================================
-- 初始化基础数据
-- ============================================

-- 插入示例用户
INSERT INTO `user` (username, password, real_name, role, email, phone) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwxi8xNa', '系统管理员', 'HR_MANAGER', 'admin@example.com', '13800138000'),
('hr01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwxi8xNa', '王管理员', 'HR_SPECIALIST', 'hr01@example.com', '13800138001'),
('hr02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwxi8xNa', '李管理员', 'HR_MANAGER', 'hr02@example.com', '13800138002'),
('salary01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwxi8xNa', '张经理', 'SALARY_SPECIALIST', 'salary01@example.com', '13800138003'),
('salary02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwxi8xNa', '李经理', 'SALARY_MANAGER', 'salary02@example.com', '13800138004');

-- 插入示例机构（三级机构）
-- 一级机构（org_id 将自动为 1, 2）
INSERT INTO organization (org_code, org_name, org_level, parent_id) VALUES
('01', '技术中心', 1, NULL),
('02', '市场中心', 1, NULL);

-- 二级机构（org_id 将自动为 3, 4, 5）
INSERT INTO organization (org_code, org_name, org_level, parent_id) VALUES
('01', '产品研发部', 2, 1),
('02', '测试部', 2, 1),
('01', '市场推广部', 2, 2);

-- 三级机构（org_id 将自动为 6, 7, 8, 9）
INSERT INTO organization (org_code, org_name, org_level, parent_id) VALUES
('01', '前端组', 3, 3),
('02', '后端组', 3, 3),
('03', '产品组', 3, 3),
('01', '测试一组', 3, 4);

-- 插入示例职位（third_org_id 引用三级机构的 org_id: 6, 7, 8, 9）
INSERT INTO `position` (position_name, third_org_id) VALUES
('前端工程师', 6),
('后端工程师', 7),
('产品经理', 8),
('测试工程师', 9);

-- 插入薪酬项目
INSERT INTO salary_item (item_code, item_name, item_type, calculation_rule, sort_order) VALUES
('S001', '基本工资', 'INCOME', NULL, 1),
('S002', '绩效奖金', 'INCOME', NULL, 2),
('S003', '交通补贴', 'INCOME', NULL, 3),
('S004', '餐费补贴', 'INCOME', NULL, 4),
('S005', '全勤奖', 'INCOME', NULL, 5),
('S006', '养老保险', 'DEDUCTION', '基本工资*8%', 6),
('S007', '医疗保险', 'DEDUCTION', '基本工资*2%+3', 7),
('S008', '失业保险', 'DEDUCTION', '基本工资*0.5%', 8),
('S009', '住房公积金', 'DEDUCTION', '基本工资*8%', 9);

