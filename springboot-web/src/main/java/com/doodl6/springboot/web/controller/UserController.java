package com.doodl6.springboot.web.controller;

import com.doodl6.springboot.dao.api.UserLoginLogMapper;
import com.doodl6.springboot.dao.api.UserMapper;
import com.doodl6.springboot.dao.entity.User;
import com.doodl6.springboot.dao.entity.UserLoginLog;
import com.doodl6.springboot.web.response.base.MapResponse;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserLoginLogMapper userLoginLogMapper;

    /**
     * 新增用户
     */
    @RequestMapping("/addUser")
    public MapResponse addUser(String name) {
        Preconditions.checkArgument(StringUtils.isNotBlank(name), "用户名称不能为空");

        User user = new User();
        user.setName(name);
        userMapper.insert(user);
        MapResponse response = new MapResponse();
        response.appendData("user", user);

        return response;
    }

    /**
     * 用户登录
     */
    @RequestMapping("/login/{userId}")
    public MapResponse addUser(@PathVariable Long userId) {
        Preconditions.checkArgument(userId != null, "用户ID不能为空");

        User user = userMapper.getById(userId);

        Preconditions.checkArgument(user != null, "用户不存在");
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setUserId(userId);
        userLoginLog.setLoginTime(new Date());
        userLoginLogMapper.insert(userLoginLog);

        MapResponse response = new MapResponse();
        List<UserLoginLog> userLoginLogList = userLoginLogMapper.queryLastLoginLog(userId);
        response.appendData("lastLogin", userLoginLogList);

        return response;
    }

}
