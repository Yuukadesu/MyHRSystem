# Storage 模块说明

## 模块职责

Storage 模块负责统一管理数据库交互，包括：
- Mapper 接口定义
- MyBatis-Plus 配置
- 数据库连接配置（需要在应用模块中配置）

## 代码结构

### Common 模块（通用代码）

#### 实体类 (Entity)
位置：`common/src/main/java/com/exemple/pojo/entity/`

包含以下9个实体类：
1. `User.java` - 用户表
2. `Organization.java` - 机构表
3. `Position.java` - 职位表
4. `SalaryItem.java` - 薪酬项目表
5. `SalaryStandard.java` - 薪酬标准表
6. `SalaryStandardItem.java` - 薪酬标准明细表
7. `EmployeeArchive.java` - 员工档案表
8. `SalaryIssuance.java` - 薪酬发放单表
9. `SalaryIssuanceDetail.java` - 薪酬发放明细表

#### 枚举类 (Enum)
位置：`common/src/main/java/com/exemple/pojo/enums/`

包含以下枚举类：
1. `UserRole.java` - 用户角色枚举
2. `UserStatus.java` - 用户状态枚举
3. `OrgStatus.java` - 机构状态枚举
4. `JobTitle.java` - 职称枚举
5. `Gender.java` - 性别枚举
6. `SalaryItemType.java` - 薪酬项目类型枚举
7. `SalaryStandardStatus.java` - 薪酬标准状态枚举
8. `EmployeeArchiveStatus.java` - 员工档案状态枚举
9. `SalaryIssuanceStatus.java` - 薪酬发放单状态枚举
10. `PaymentStatus.java` - 付款状态枚举

### Storage 模块（数据库交互）

#### Mapper 接口
位置：`storage/src/main/java/com/exemple/mapper/`

包含以下9个 Mapper 接口：
1. `UserMapper.java` - 用户表 Mapper
2. `OrganizationMapper.java` - 机构表 Mapper
3. `PositionMapper.java` - 职位表 Mapper
4. `SalaryItemMapper.java` - 薪酬项目表 Mapper
5. `SalaryStandardMapper.java` - 薪酬标准表 Mapper
6. `SalaryStandardItemMapper.java` - 薪酬标准明细表 Mapper
7. `EmployeeArchiveMapper.java` - 员工档案表 Mapper
8. `SalaryIssuanceMapper.java` - 薪酬发放单表 Mapper
9. `SalaryIssuanceDetailMapper.java` - 薪酬发放明细表 Mapper

所有 Mapper 接口继承 `BaseMapper<T>`，提供基础的 CRUD 操作：
- `insert(T entity)` - 插入
- `deleteById(Serializable id)` - 根据ID删除
- `updateById(T entity)` - 根据ID更新
- `selectById(Serializable id)` - 根据ID查询
- `selectList(Wrapper<T> queryWrapper)` - 条件查询列表
- `selectPage(IPage<T> page, Wrapper<T> queryWrapper)` - 分页查询

#### 配置类
位置：`storage/src/main/java/com/exemple/config/`

- `MyBatisPlusConfig.java` - MyBatis-Plus 配置类，包含分页插件配置

## 使用方式

### 1. 在其他模块中使用

在需要使用数据库交互的模块（如 `authorization-management`、`human-resource-archive-management` 等）的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.exemple</groupId>
    <artifactId>storage</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 配置数据库连接

在 `application.yml` 或 `application.properties` 中配置：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/myHRSystem?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

### 3. 使用 Mapper

在 Service 中注入 Mapper 使用：

```java
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    // 使用条件查询
    public List<User> getActiveUsers() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("status", UserStatus.ACTIVE.getCode());
        return userMapper.selectList(wrapper);
    }
}
```

### 4. 使用枚举类

```java
// 设置用户角色
user.setRole(UserRole.HR_MANAGER.getCode());

// 判断用户状态
if (user.getStatus().equals(UserStatus.ACTIVE.getCode())) {
    // 处理激活用户
}
```

## 扩展说明

### 自定义查询方法

如果需要在 Mapper 中添加自定义查询方法，可以：

1. **使用 MyBatis-Plus 的 Wrapper**
   ```java
   QueryWrapper<User> wrapper = new QueryWrapper<>();
   wrapper.like("username", "admin")
          .eq("status", UserStatus.ACTIVE.getCode());
   List<User> users = userMapper.selectList(wrapper);
   ```

2. **在 Mapper 接口中定义方法，使用 @Select 注解**
   ```java
   @Select("SELECT * FROM user WHERE username = #{username}")
   User selectByUsername(String username);
   ```

3. **创建 XML 映射文件**（复杂查询时）
   - 在 `resources/mapper/` 目录下创建对应的 XML 文件
   - 在 XML 中编写 SQL 语句

## 注意事项

1. 所有实体类都使用 `@TableName` 注解指定表名
2. 主键字段使用 `@TableId(type = IdType.AUTO)` 指定自增策略
3. 字段名使用驼峰命名，数据库字段使用下划线命名，MyBatis-Plus 会自动转换
4. 枚举类提供了 `fromCode()` 方法，方便从数据库值转换为枚举
5. Mapper 接口需要添加 `@Mapper` 注解，或在启动类上添加 `@MapperScan("com.exemple.mapper")`

