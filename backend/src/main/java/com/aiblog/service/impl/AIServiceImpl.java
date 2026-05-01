package com.aiblog.service.impl;

import com.aiblog.service.AIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

    private final ChatClient chatClient;

    public AIServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("你是一个专业的博客助手，擅长为技术文章生成简洁的摘要和精准的标签。")
                .build();
    }

    @Override
    public String generateSummary(String content) {
        String prompt = String.format(
                "请为以下博客文章生成一段简洁的摘要（100字以内），只输出摘要内容，不要有其他说明：\n\n%s",
                truncateContent(content, 3000)
        );

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @Override
    public List<String> generateTags(String content) {
        String prompt = String.format(
                "请为以下博客文章生成3-5个精准的标签，标签之间用逗号分隔，只输出标签列表，不要有其他说明：\n\n%s",
                truncateContent(content, 3000)
        );

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return Arrays.stream(response.split("[,，]"))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .limit(5)
                .collect(Collectors.toList());
    }

    private String truncateContent(String content, int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
}
