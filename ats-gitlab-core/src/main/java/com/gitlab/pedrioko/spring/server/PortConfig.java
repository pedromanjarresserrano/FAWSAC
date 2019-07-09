package com.gitlab.pedrioko.spring.server;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PortConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Value("#{'${server.ports}'.split(',')}")
    private List<String> myList;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        myList.forEach(e -> {
            Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            connector.setPort(Integer.valueOf(e));
            factory.addAdditionalTomcatConnectors(connector);
        });
    }
}