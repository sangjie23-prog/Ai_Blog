package com.aiblog.config;

import com.aiblog.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web配置类
 * 注册JWT拦截器，配置需要拦截的路径
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Value("${upload.path:uploads/images}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有/admin/**路径（后台管理接口）
                .addPathPatterns("/api/admin/**")
                // 排除登录接口
                .excludePathPatterns("/api/auth/**")
                // 排除前台公开接口
                .excludePathPatterns("/api/articles/**")
                // 排除图片上传接口
                .excludePathPatterns("/api/admin/upload/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File basePath = new File(uploadPath);
        String absolutePath = basePath.isAbsolute() ? uploadPath : System.getProperty("user.dir") + File.separator + uploadPath;
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .setCachePeriod(86400);
    }
}
