package com.liyuyouguo.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池
 *
 * @author baijianmin
 */
@Slf4j
public class FruitShopThreadPool {

    /**
     * 服务器CPU核心数
     */
    private static final int CORES = Runtime.getRuntime().availableProcessors();

    /**
     * 核心线程数 = 服务器CPU核心数
     */
    private static final int CORE_POOL_SIZE = CORES;

    /**
     * 最大线程数 =  服务器CPU核心数 * 2
     */
    private static final int MAX_POOL_SIZE = CORES * 2;

    /**
     * 队列大小
     */
    private static final int QUEUE_CAPACITY = 1000;

    /**
     * 线程池中的线程的名称前缀
     */
    private static final String THREAD_NAME = "css-thread-";

    private FruitShopThreadPool() {
    }

    /**
     * 初始化线程池
     *
     * @return Executor 线程池对象
     */
    public static Executor init() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 可自定义线程拒绝策略的线程池
     *
     * @param handler 自定义拒绝策略
     * @return Executor 线程池对象
     */
    public static Executor init(RejectedExecutionHandler handler) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(handler);
        executor.initialize();
        return executor;
    }
}
