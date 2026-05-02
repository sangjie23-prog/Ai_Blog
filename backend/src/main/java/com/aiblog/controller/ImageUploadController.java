package com.aiblog.controller;

import com.aiblog.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class ImageUploadController {

    @Value("${upload.path:uploads/images}")
    private String uploadPathConfig;

    private String uploadPath;

    @Value("${upload.max-size:10485760}")
    private long maxFileSize;

    @jakarta.annotation.PostConstruct
    public void init() {
        File basePath = new File(uploadPathConfig);
        if (basePath.isAbsolute()) {
            uploadPath = uploadPathConfig;
        } else {
            uploadPath = System.getProperty("user.dir") + File.separator + uploadPathConfig;
        }
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static final String[] ALLOWED_TYPES = {
        "image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"
    };

    @PostMapping("/upload/image")
    public Result<?> uploadImage(@RequestParam("file") MultipartFile file) {
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

            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String fullUploadPath = uploadPath + "/" + datePath;

            File uploadDir = new File(fullUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(fullUploadPath + "/" + newFilename);
            file.transferTo(destFile);

            String imageUrl = "/api/uploads/images/" + datePath + "/" + newFilename;

            return Result.success("图片上传成功", imageUrl);
        } catch (IOException e) {
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }
}
