package com.aiblog.service.impl;

import com.aiblog.common.JwtUtil;
import com.aiblog.entity.User;
import com.aiblog.mapper.UserMapper;
import com.aiblog.service.AuthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String login(String username, String password) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        // 用户不存在
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 密码验证（MVP阶段直接比较，后续应使用加密）
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成Token
        return jwtUtil.generateToken(user.getId(), user.getUsername());
    }
}
