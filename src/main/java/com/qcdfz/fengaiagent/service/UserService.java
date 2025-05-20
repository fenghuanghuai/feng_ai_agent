package com.qcdfz.fengaiagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcdfz.fengaiagent.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.service
 * @author: fgh
 * @description: 用户服务接口
 * @createTime: 2025-05-17 21:32
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request HTTP请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request HTTP请求
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user 用户
     * @return 是否为管理员
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean userLogout(HttpServletRequest request);
}