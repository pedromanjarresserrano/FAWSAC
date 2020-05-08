package com.gitlab.pedrioko.spring.server;

import lombok.Data;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public @Data
class ServerListener implements ApplicationListener<ServletWebServerInitializedEvent> {
    private int intPort = 8080;

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        intPort = event.getWebServer().getPort();
    }
}