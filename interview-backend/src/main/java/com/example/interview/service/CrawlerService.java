package com.example.interview.service;

import java.util.List;
import java.util.Map;

public interface CrawlerService {

    Map<String, Object> crawlFromNowCoder(String url);

    Map<String, Object> crawlFromLeetCode(String url);

    Map<String, Object> crawlFromCSDN(String url);

    Map<String, Object> crawlFromZhihu(String url);

    List<Map<String, Object>> batchCrawl(List<String> urls);

    int crawlAllSources();
}