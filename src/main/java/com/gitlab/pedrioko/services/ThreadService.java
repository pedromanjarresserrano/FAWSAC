package com.gitlab.pedrioko.services;

public interface ThreadService {
    void addProcess(Runnable runnable);

    String getCurrent();

    void setCurrent(String current);
}
