# SQL文件导入说明

## 方法一：使用命令行导入（推荐）

```bash
# 方式1：使用mysql命令导入
mysql -u root -p myHRSystem < myHRSystem.sql

# 方式2：如果数据库不存在，先创建数据库，然后导入
mysql -u root -p < myHRSystem.sql
```

## 方法二：使用MySQL客户端图形界面

1. 打开MySQL客户端（如MySQL Workbench、Navicat等）
2. 连接到MySQL服务器
3. 打开文件：`myHRSystem.sql`
4. 执行整个脚本

## 方法三：在MySQL命令行中执行

```sql
-- 连接到MySQL
mysql -u root -p

-- 在MySQL命令行中执行
SOURCE /path/to/myHRSystem.sql;
```

或者：

```sql
-- 连接到MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE IF NOT EXISTS myHRSystem DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE myHRSystem;

-- 执行SQL文件
SOURCE /Users/hanami/IdeaProjects/myHRSystem/storage/src/main/resources/database/myHRSystem.sql;
```

## 验证导入是否成功

```sql
-- 连接到数据库
mysql -u root -p myHRSystem

-- 查看所有表
SHOW TABLES;

-- 应该看到以下9张表：
-- user
-- organization
-- position
-- salary_item
-- salary_standard
-- salary_standard_item
-- employee_archive
-- salary_issuance
-- salary_issuance_detail

-- 检查数据是否导入成功
SELECT COUNT(*) FROM `user`;  -- 应该返回5
SELECT COUNT(*) FROM organization;  -- 应该返回9
SELECT COUNT(*) FROM position;  -- 应该返回4
SELECT COUNT(*) FROM salary_item;  -- 应该返回9
```

## 常见问题

### 1. 外键约束错误
如果遇到外键约束错误，SQL文件已经包含了 `SET FOREIGN_KEY_CHECKS = 0/1` 来处理这个问题。

### 2. 数据库已存在
SQL文件使用 `CREATE DATABASE IF NOT EXISTS`，不会覆盖已存在的数据库。如果需要重新创建，请先删除：
```sql
DROP DATABASE IF EXISTS myHRSystem;
```

### 3. 字符编码问题
数据库使用 `utf8mb4` 字符集，支持中文和emoji。如果导入时出现乱码，请检查MySQL客户端的字符集设置。

### 4. 权限问题
确保MySQL用户有创建数据库和表的权限：
```sql
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

## 文件说明

- `myHRSystem.sql` - 完整的数据库建表脚本，包含：
  - 数据库创建语句
  - 9张表的创建语句
  - 外键约束和索引
  - 初始化示例数据

- `DATABASE_DESIGN.md` - 详细的数据库设计文档

- `README.md` - 快速开始指南

