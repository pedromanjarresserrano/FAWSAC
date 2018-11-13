package com.gitlab.pedrioko.spring;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

//@Configuration
public class PortConfig {
    @Value("#{'${server.ports}'.split(',')}")
    private List<String> myList;

   /*@Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) container;
                myList.forEach(e -> {
                    Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
                    connector.setPort(Integer.valueOf(e));
                    containerFactory.addAdditionalTomcatConnectors(connector);
                });
            }
        };
    }*/
}