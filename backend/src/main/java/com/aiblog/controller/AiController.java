package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/ai")
public class AiController {

    @Autowired
    private AIService aiService;

    @PostMapping("/summary")
    public Result<String> generateSummary(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        if (content == null || content.trim().isEmpty()) {
            return Result.error(400, "文章内容不能为空");
        }
        String summary = aiService.generateSummary(content);
        return Result.success(summary);
    }

    @PostMapping("/tags")
    public Result<List<String>> generateTags(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        if (content == null || content.trim().isEmpty()) {
            return Result.error(400, "文章内容不能为空");
        }
        List<String> tags = aiService.generateTags(content);
        return Result.success(tags);
    }

    @PostMapping("/generate")
    public Result<Map<String, Object>> generateAll(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        if (content == null || content.trim().isEmpty()) {
            return Result.error(400, "文章内容不能为空");
        }

        String summary = aiService.generateSummary(content);
        List<String> tags = aiService.generateTags(content);

        Map<String, Object> result = new HashMap<>();
        result.put("summary", summary);
        result.put("tags", tags);

        return Result.success(result);
    }
}
