package com.yun.usercenterserver.service;

import com.yun.usercenterserver.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author yun
 * @createDate 2025-07-27 16:01:45
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount       用户账户
     * @param userPassword      用户密码
     * @param checkPassword     校验密码
     * @return  新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登陆
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param request       用于封装客户端发送给服务器的 HTTP 请求信息
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     */
    int userLogout(HttpServletRequest request);

    /**
     *  用户数据脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
}
