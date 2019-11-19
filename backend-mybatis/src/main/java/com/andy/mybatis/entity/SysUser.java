package com.andy.mybatis.entity;

    import com.baomidou.mybatisplus.annotation.TableName;
    import java.time.LocalDateTime;
    import com.andy.mybatis.entity.BaseEntity;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 系统用户表
    * </p>
*
* @author Generator
* @since 2019-11-19
*/
    @Data
        @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    @TableName("sys_user")
    public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

            /**
            * 账号
            */
    private String account;

            /**
            * 密码
            */
    private String password;

            /**
            * 昵称
            */
    private String nickName;

            /**
            * 职务
            */
    private String duty;

            /**
            * 部门
            */
    private Integer deptId;

            /**
            * 0 离职 1 在职
            */
    private Integer status;

            /**
            * 性别 0 女 1 男
            */
    private Integer sex;

            /**
            * 年龄
            */
    private Integer age;

            /**
            * 手机号码
            */
    private String tel;

            /**
            * email
            */
    private String email;



}
