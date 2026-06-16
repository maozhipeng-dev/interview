package com.example.interview.controller;

import com.example.interview.dto.ResponseDTO;
import com.example.interview.service.CrawlerService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crawler")
@Tag(name = "爬虫管理", description = "面试题爬虫接口")
public class CrawlerController {

    private final CrawlerService crawlerService;

    @Autowired
    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/nowcoder")
    @Operation(summary = "爬取牛客网", description = "从牛客网爬取面试题")
    @RateLimiter(name = "crawler", fallbackMethod = "crawlFallback")
    public ResponseDTO<Map<String, Object>> crawlNowCoder(
            @Parameter(description = "牛客网URL") @RequestParam String url) {
        Map<String, Object> result = crawlerService.crawlFromNowCoder(url);
        if ((Boolean) result.get("success")) {
            return ResponseDTO.success(result);
        } else {
            return ResponseDTO.error((String) result.get("message"));
        }
    }

    @GetMapping("/leetcode")
    @Operation(summary = "爬取LeetCode", description = "从LeetCode爬取面试题")
    @RateLimiter(name = "crawler", fallbackMethod = "crawlFallback")
    public ResponseDTO<Map<String, Object>> crawlLeetCode(
            @Parameter(description = "LeetCode URL") @RequestParam String url) {
        Map<String, Object> result = crawlerService.crawlFromLeetCode(url);
        if ((Boolean) result.get("success")) {
            return ResponseDTO.success(result);
        } else {
            return ResponseDTO.error((String) result.get("message"));
        }
    }

    @GetMapping("/csdn")
    @Operation(summary = "爬取CSDN", description = "从CSDN爬取面试题")
    @RateLimiter(name = "crawler", fallbackMethod = "crawlFallback")
    public ResponseDTO<Map<String, Object>> crawlCSDN(
            @Parameter(description = "CSDN URL") @RequestParam String url) {
        Map<String, Object> result = crawlerService.crawlFromCSDN(url);
        if ((Boolean) result.get("success")) {
            return ResponseDTO.success(result);
        } else {
            return ResponseDTO.error((String) result.get("message"));
        }
    }

    @GetMapping("/zhihu")
    @Operation(summary = "爬取知乎", description = "从知乎爬取面试题")
    @RateLimiter(name = "crawler", fallbackMethod = "crawlFallback")
    public ResponseDTO<Map<String, Object>> crawlZhihu(
            @Parameter(description = "知乎URL") @RequestParam String url) {
        Map<String, Object> result = crawlerService.crawlFromZhihu(url);
        if ((Boolean) result.get("success")) {
            return ResponseDTO.success(result);
        } else {
            return ResponseDTO.error((String) result.get("message"));
        }
    }

    @PostMapping("/batch")
    @Operation(summary = "批量爬取", description = "批量爬取多个URL的面试题")
    @RateLimiter(name = "crawler", fallbackMethod = "batchCrawlFallback")
    public ResponseDTO<List<Map<String, Object>>> batchCrawl(@RequestBody List<String> urls) {
        List<Map<String, Object>> results = crawlerService.batchCrawl(urls);
        return ResponseDTO.success(results);
    }

    @GetMapping("/auto")
    @Operation(summary = "自动爬取", description = "自动爬取所有预设源的面试题")
    @RateLimiter(name = "crawler", fallbackMethod = "autoCrawlFallback")
    public ResponseDTO<Map<String, Object>> crawlAll() {
        int count = crawlerService.crawlAllSources();
        return ResponseDTO.success(Map.of("success", true, "count", count, "message", "自动爬取完成，共获取 " + count + " 条新数据"));
    }

    public ResponseDTO<Map<String, Object>> crawlFallback(String url, Exception e) {
        return ResponseDTO.error("请求过于频繁，请稍后再试");
    }

    public ResponseDTO<List<Map<String, Object>>> batchCrawlFallback(List<String> urls, Exception e) {
        return ResponseDTO.error("请求过于频繁，请稍后再试");
    }

    public ResponseDTO<Map<String, Object>> autoCrawlFallback(Exception e) {
        return ResponseDTO.error("请求过于频繁，请稍后再试");
    }
}