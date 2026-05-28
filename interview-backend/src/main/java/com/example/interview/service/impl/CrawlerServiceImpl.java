package com.example.interview.service.impl;

import com.example.interview.entity.Question;
import com.example.interview.repository.QuestionRepository;
import com.example.interview.service.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CrawlerServiceImpl implements CrawlerService {

    private final QuestionRepository questionRepository;

    @Autowired
    public CrawlerServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final Random random = new Random();

    private Document getDocument(String url) throws IOException {
        try {
            Thread.sleep(random.nextInt(2000) + 1000);
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(30000)
                    .followRedirects(true)
                    .get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("爬虫被中断", e);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> crawlFromNowCoder(String url) {
        Map<String, Object> result = new HashMap<>();
        List<Question> savedQuestions = new ArrayList<>();
        
        try {
            Document doc = getDocument(url);
            Elements questions = doc.select(".question-list .question-item");
            
            for (Element item : questions) {
                try {
                    String title = item.select(".question-title").text().trim();
                    String answer = item.select(".question-answer").text().trim();
                    String category = item.select(".question-tag").text().trim();
                    String difficultyStr = item.select(".difficulty").text().trim();
                    
                    if (title.isEmpty() || answer.isEmpty()) continue;
                    
                    Question question = createQuestion(title, answer, category, difficultyStr);
                    question.setSourceUrl(url);
                    question.setSourceName("牛客网");
                    
                    if (!existsByTitle(title)) {
                        question = questionRepository.save(question);
                        savedQuestions.add(question);
                    }
                } catch (Exception e) {
                    log.warn("解析牛客网题目失败: {}", e.getMessage());
                }
            }
            
            result.put("success", true);
            result.put("message", "爬取成功");
            result.put("count", savedQuestions.size());
            result.put("questions", savedQuestions);
            
        } catch (IOException e) {
            log.error("爬取牛客网失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "爬取失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> crawlFromLeetCode(String url) {
        Map<String, Object> result = new HashMap<>();
        List<Question> savedQuestions = new ArrayList<>();
        
        try {
            Document doc = getDocument(url);
            Elements problems = doc.select(".question-summary");
            
            for (Element item : problems) {
                try {
                    String title = item.select(".question-title a").text().trim();
                    String difficultyStr = item.select(".difficulty-label").text().trim();
                    String tags = item.select(".tags").text().trim();
                    
                    if (title.isEmpty()) continue;
                    
                    Question question = createQuestion(title, "请访问原题查看解答", tags, difficultyStr);
                    question.setSourceUrl("https://leetcode.cn" + item.select(".question-title a").attr("href"));
                    question.setSourceName("LeetCode");
                    
                    if (!existsByTitle(title)) {
                        question = questionRepository.save(question);
                        savedQuestions.add(question);
                    }
                } catch (Exception e) {
                    log.warn("解析LeetCode题目失败: {}", e.getMessage());
                }
            }
            
            result.put("success", true);
            result.put("message", "爬取成功");
            result.put("count", savedQuestions.size());
            result.put("questions", savedQuestions);
            
        } catch (IOException e) {
            log.error("爬取LeetCode失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "爬取失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> crawlFromCSDN(String url) {
        Map<String, Object> result = new HashMap<>();
        List<Question> savedQuestions = new ArrayList<>();
        
        try {
            Document doc = getDocument(url);
            Elements articles = doc.select(".article-item-box");
            
            for (Element item : articles) {
                try {
                    String title = item.select(".title a").text().trim();
                    String summary = item.select(".content").text().trim();
                    String category = item.select(".category").text().trim();
                    
                    if (title.isEmpty()) continue;
                    
                    Question question = createQuestion(title, summary, category.isEmpty() ? "技术文章" : category, "3");
                    question.setSourceUrl(item.select(".title a").attr("href"));
                    question.setSourceName("CSDN");
                    
                    if (!existsByTitle(title)) {
                        question = questionRepository.save(question);
                        savedQuestions.add(question);
                    }
                } catch (Exception e) {
                    log.warn("解析CSDN文章失败: {}", e.getMessage());
                }
            }
            
            result.put("success", true);
            result.put("message", "爬取成功");
            result.put("count", savedQuestions.size());
            result.put("questions", savedQuestions);
            
        } catch (IOException e) {
            log.error("爬取CSDN失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "爬取失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> crawlFromZhihu(String url) {
        Map<String, Object> result = new HashMap<>();
        List<Question> savedQuestions = new ArrayList<>();
        
        try {
            Document doc = getDocument(url);
            Elements items = doc.select(".List-item");
            
            for (Element item : items) {
                try {
                    String title = item.select(".QuestionTitle").text().trim();
                    String excerpt = item.select(".RichContent").text().trim();
                    
                    if (title.isEmpty()) continue;
                    
                    Question question = createQuestion(title, excerpt, "问答", "3");
                    question.setSourceUrl("https://www.zhihu.com" + item.select(".QuestionTitle").attr("href"));
                    question.setSourceName("知乎");
                    
                    if (!existsByTitle(title)) {
                        question = questionRepository.save(question);
                        savedQuestions.add(question);
                    }
                } catch (Exception e) {
                    log.warn("解析知乎内容失败: {}", e.getMessage());
                }
            }
            
            result.put("success", true);
            result.put("message", "爬取成功");
            result.put("count", savedQuestions.size());
            result.put("questions", savedQuestions);
            
        } catch (IOException e) {
            log.error("爬取知乎失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "爬取失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public List<Map<String, Object>> batchCrawl(List<String> urls) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (String url : urls) {
            Map<String, Object> result = crawlByUrl(url);
            results.add(result);
        }
        
        return results;
    }

    private Map<String, Object> crawlByUrl(String url) {
        if (url.contains("nowcoder")) {
            return crawlFromNowCoder(url);
        } else if (url.contains("leetcode") || url.contains("leetcode-cn")) {
            return crawlFromLeetCode(url);
        } else if (url.contains("csdn")) {
            return crawlFromCSDN(url);
        } else if (url.contains("zhihu")) {
            return crawlFromZhihu(url);
        } else {
            return crawlGeneric(url);
        }
    }

    private Map<String, Object> crawlGeneric(String url) {
        Map<String, Object> result = new HashMap<>();
        List<Question> savedQuestions = new ArrayList<>();
        
        try {
            Document doc = getDocument(url);
            String title = doc.title();
            String content = doc.body().text();
            
            if (!title.isEmpty()) {
                Question question = createQuestion(title, content.substring(0, Math.min(content.length(), 500)), "其他", "3");
                question.setSourceUrl(url);
                question.setSourceName("通用爬虫");
                
                if (!existsByTitle(title)) {
                    question = questionRepository.save(question);
                    savedQuestions.add(question);
                }
            }
            
            result.put("success", true);
            result.put("message", "爬取成功");
            result.put("count", savedQuestions.size());
            
        } catch (IOException e) {
            log.error("通用爬虫失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "爬取失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public int crawlAllSources() {
        int total = 0;
        
        String[] nowcoderUrls = {
            "https://www.nowcoder.com/discuss/tag/63",
            "https://www.nowcoder.com/discuss/tag/176"
        };
        
        String[] leetcodeUrls = {
            "https://leetcode.cn/problemset/all/",
            "https://leetcode.cn/tag/array/"
        };
        
        for (String url : nowcoderUrls) {
            Map<String, Object> result = crawlFromNowCoder(url);
            total += (Integer) result.get("count");
        }
        
        for (String url : leetcodeUrls) {
            Map<String, Object> result = crawlFromLeetCode(url);
            total += (Integer) result.get("count");
        }
        
        return total;
    }

    private Question createQuestion(String title, String answer, String category, String difficultyStr) {
        Question question = new Question();
        question.setTitle(title);
        question.setAnswer(answer);
        question.setCategory(category.isEmpty() ? "未分类" : category);
        question.setDifficulty(parseDifficulty(difficultyStr));
        question.setTags(category);
        question.setIsActive(true);
        return question;
    }

    private Integer parseDifficulty(String difficultyStr) {
        if (difficultyStr == null || difficultyStr.isEmpty()) {
            return 3;
        }
        
        difficultyStr = difficultyStr.toLowerCase();
        
        if (difficultyStr.contains("简单") || difficultyStr.contains("easy")) {
            return 1;
        } else if (difficultyStr.contains("中等") || difficultyStr.contains("medium")) {
            return 3;
        } else if (difficultyStr.contains("困难") || difficultyStr.contains("hard")) {
            return 5;
        }
        
        Matcher matcher = Pattern.compile("(\\d+)").matcher(difficultyStr);
        if (matcher.find()) {
            return Math.min(Math.max(Integer.parseInt(matcher.group(1)), 1), 5);
        }
        
        return 3;
    }

    private boolean existsByTitle(String title) {
        return questionRepository.existsByTitle(title);
    }
}