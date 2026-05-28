package com.example.interview.controller;

import com.example.interview.dto.ResponseDTO;
import com.example.interview.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {

    private final CrawlerService crawlerService;

    @Autowired
    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/nowcoder")
    public ResponseDTO<Map<String, Object>> crawlNowCoder(@RequestParam String url) {
        Map<String, Object> result = crawlerService.crawlFromNowCoder(url);
        if ((Boolean) result.get("success")) {
            return ResponseDTO.success(result);
        } else {
            return ResponseDTO.error((String) result.get("message"));
        }
    }

    @GetMapping("/leetcode")
    public ResponseDTO<Map<String, Object>> crawlLeetCode(@RequestParam String url) {
        Map<String, Object> result = crawlerService.crawlFromLeetCode(url);
        if ((Boolean) result.get("success")) {
            return ResponseDTO.success(result);
        } else {
            return ResponseDTO.error((String) result.get("message"));
        }
    }

    @GetMapping("/csdn")
    public ResponseDTO<Map<String, Object>> crawlCSDN(@RequestParam String url) {
        Map<String, Object> result = crawlerService.crawlFromCSDN(url);
        if ((Boolean) result.get("success")) {
            return ResponseDTO.success(result);
        } else {
            return ResponseDTO.error((String) result.get("message"));
        }
    }

    @GetMapping("/zhihu")
    public ResponseDTO<Map<String, Object>> crawlZhihu(@RequestParam String url) {
        Map<String, Object> result = crawlerService.crawlFromZhihu(url);
        if ((Boolean) result.get("success")) {
            return ResponseDTO.success(result);
        } else {
            return ResponseDTO.error((String) result.get("message"));
        }
    }

    @PostMapping("/batch")
    public ResponseDTO<List<Map<String, Object>>> batchCrawl(@RequestBody List<String> urls) {
        List<Map<String, Object>> results = crawlerService.batchCrawl(urls);
        return ResponseDTO.success(results);
    }

    @GetMapping("/auto")
    public ResponseDTO<Map<String, Object>> crawlAll() {
        int count = crawlerService.crawlAllSources();
        return ResponseDTO.success(Map.of("success", true, "count", count, "message", "自动爬取完成，共获取 " + count + " 条新数据"));
    }
}