package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供登录等认证相关接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 管理员登录
     * @param loginRequest 登录请求体 {username, password}
     * @return JWT Token
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // 参数校验
        if (username == null || password == null) {
            return Result.error(400, "用户名和密码不能为空");
        }

        try {
            // 执行登录
            String token = authService.login(username, password);

            // 返回Token
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", username);
            return Result.success("登录成功", data);
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }
}
