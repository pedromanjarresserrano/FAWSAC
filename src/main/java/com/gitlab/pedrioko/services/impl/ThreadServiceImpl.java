package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.services.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ThreadServiceImpl implements ThreadService {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private String current = "";

    @Override
    public void addProcess(Runnable runnable) {
        threadPoolTaskExecutor.execute(runnable);
    }

    @Override
    public String getCurrent() {
        return current;
    }

    @Override
    public void setCurrent(String current) {
        this.current = current;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() throws IllegalStateException {
        return threadPoolTaskExecutor.getThreadPoolExecutor();
    }

    public long getCompletedTaskCount() throws IllegalStateException {
        return getThreadPoolExecutor().getCompletedTaskCount();
    }

    public int getQueueSize() throws IllegalStateException {
        return getThreadPoolExecutor().getQueue().size();
    }

    public long getTaskCount() throws IllegalStateException {
        return getThreadPoolExecutor().getTaskCount();
    }
}
