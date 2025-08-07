package com.yun.usercenterserver.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
// Mapper 接口继承了 MyBatis-Plus 的 BaseMapper<User>，
// 则必须通过 @TableName("表名") 告诉 MyBatis-Plus 对应的表名，
// 否则它无法自动生成 SQL（会默认用实体类名当表名，可能与数据库表名不一致）
@TableName(value ="user")
@Data
// 实体类如何与数据库中的表绑定到一起的
public class User {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarURL;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户状态 " 0 - 正常 "
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     * @TableLogic 用于实现逻辑删除，（通过mybatis-plus）
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    /**
     * 火葬编号
     */
    private  String planetCode;
}