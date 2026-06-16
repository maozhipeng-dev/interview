package com.example.interview.config;

import com.example.interview.entity.Question;
import com.example.interview.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    @Autowired
    public DataInitializer(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void run(String... args) {
        if (questionRepository.count() == 0) {
            questionRepository.save(new Question(null, "什么是Spring Boot的自动配置?", "Spring Boot的自动配置是一种机制，它能根据项目中添加的依赖自动配置Spring应用。通过@EnableAutoConfiguration注解，Spring Boot会扫描classpath中的META-INF/spring.factories文件，加载对应的自动配置类。自动配置可以大大简化Spring应用的配置过程，减少样板代码。", "Spring Boot", 2, "自动配置,注解", true));

            questionRepository.save(new Question(null, "Java中equals()和==的区别?", "==用于比较两个对象的引用是否指向同一个内存地址，即比较对象的身份。equals()方法用于比较两个对象的内容是否相等。默认情况下，Object类的equals()方法与==相同，但很多类（如String、Integer）重写了equals()方法来比较内容。", "Java基础", 1, "equals,==,比较", true));

            questionRepository.save(new Question(null, "Vue3中的响应式原理是什么?", "Vue3使用Proxy对象实现响应式系统。当数据被访问时，会通过track()函数收集依赖；当数据被修改时，会通过trigger()函数触发更新。相比Vue2的Object.defineProperty，Proxy可以监听数组变化和对象属性的添加/删除，性能更好且功能更完善。", "Vue3", 2, "响应式,Proxy,Reactive", true));

            questionRepository.save(new Question(null, "什么是RESTful API?", "REST是一种软件架构风格，RESTful API遵循REST原则设计。主要特点包括：无状态通信、统一接口、资源标识、通过HTTP方法（GET/POST/PUT/DELETE）操作资源、支持多种数据格式（JSON/XML）。RESTful API易于理解和扩展，是当前最流行的API设计风格。", "API设计", 1, "REST,API,HTTP", true));

            questionRepository.save(new Question(null, "Java中的线程池有哪些核心参数?", "线程池的核心参数包括：corePoolSize（核心线程数）、maximumPoolSize（最大线程数）、keepAliveTime（空闲线程存活时间）、workQueue（任务队列）、threadFactory（线程工厂）、handler（拒绝策略）。合理配置这些参数可以提高系统性能和稳定性。", "Java并发", 3, "线程池,ExecutorService", true));

            questionRepository.save(new Question(null, "Vue3中ref和reactive的区别?", "ref用于基本类型数据的响应式处理，通过.value访问和修改值；reactive用于对象类型数据的响应式处理，可以直接访问和修改属性。ref内部会将基本类型包装成对象，而reactive直接使用Proxy代理对象。对于嵌套对象，两者都能实现深层响应式。", "Vue3", 2, "ref,reactive,响应式", true));

            questionRepository.save(new Question(null, "Spring中的依赖注入方式有哪些?", "Spring支持三种依赖注入方式：构造器注入、Setter注入、字段注入。构造器注入是推荐方式，确保依赖在对象创建时就被初始化，保证对象的不可变性和完整性。Setter注入适合可选依赖，字段注入使用@Autowired注解，虽然简洁但不利于测试。", "Spring", 2, "依赖注入,DI,@Autowired", true));

            questionRepository.save(new Question(null, "什么是Java内存模型(JMM)?", "Java内存模型定义了Java程序中各种变量的访问规则，以及在并发环境下对这些变量进行读写操作的规范。JMM规定了主内存和工作内存的概念，所有变量存储在主内存中，每个线程有自己的工作内存。线程对变量的操作必须在工作内存中进行，不能直接操作主内存。", "Java并发", 4, "JMM,内存模型,并发", true));

            questionRepository.save(new Question(null, "Vue3的Composition API相比Options API有什么优势?", "Composition API提供了更好的代码组织方式，可以将相关逻辑组合在一起，提高代码复用性。相比Options API的分散式组织，Composition API更适合大型项目。同时，Composition API支持TypeScript，提供更好的类型推断。", "Vue3", 2, "Composition API,Options API", true));

            questionRepository.save(new Question(null, "什么是事务的ACID特性?", "事务的ACID特性包括：原子性(Atomicity)、一致性(Consistency)、隔离性(Isolation)、持久性(Durability)。原子性确保事务要么全部执行要么全部回滚；一致性确保事务前后数据完整性；隔离性确保多个事务并发执行时互不干扰；持久性确保事务提交后数据永久保存。", "数据库", 2, "事务,ACID,数据库", true));
        }
    }
}