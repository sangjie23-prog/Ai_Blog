package com.aiblog.controller;

import com.aiblog.common.JwtUtil;
import com.aiblog.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class ImageUploadController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${upload.path:uploads/images}")
    private String uploadPath;

    @Value("${upload.max-size:10485760}")
    private long maxFileSize;

    private static final String[] ALLOWED_TYPES = {
        "image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"
    };

    @PostMapping("/upload/image")
    public Result<?> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !JwtUtil.validateToken(token, jwtSecret)) {
            return Result.error("未登录或登录已过期");
        }

        if (file.isEmpty()) {
            return Result.error("请选择要上传的图片");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            return Result.error("无法识别文件类型");
        }

        boolean isAllowed = false;
        for (String allowed : ALLOWED_TYPES) {
            if (contentType.equals(allowed)) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            return Result.error("仅支持 JPG、PNG、GIF、WebP、BMP 格式的图片");
        }

        if (file.getSize() > maxFileSize) {
            return Result.error("图片大小不能超过 " + (maxFileSize / 1024 / 1024) + "MB");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;

            String datePath = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date());
            String fullUploadPath = uploadPath + "/" + datePath;

            File uploadDir = new File(fullUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(fullUploadPath + "/" + newFilename);
            file.transferTo(destFile);

            String imageUrl = "/uploads/images/" + datePath + "/" + newFilename;

            return Result.success("图片上传成功", imageUrl);
        } catch (IOException e) {
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }
}
