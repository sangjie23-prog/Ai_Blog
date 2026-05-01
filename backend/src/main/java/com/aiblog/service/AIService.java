package com.aiblog.service;

import java.util.List;

public interface AIService {

    String generateSummary(String content);

    List<String> generateTags(String content);
}
