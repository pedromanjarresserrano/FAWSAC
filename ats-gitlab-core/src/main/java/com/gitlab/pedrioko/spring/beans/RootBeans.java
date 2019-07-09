package com.gitlab.pedrioko.spring.beans;

import com.gitlab.pedrioko.config.AppInfo;
import com.gitlab.pedrioko.config.ZKCEConfig;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

@Configuration
public class RootBeans {
    private static final Logger LOGGER = LoggerFactory.getLogger(RootBeans.class);

    @Autowired
    private Environment environment;

    @Bean
    public AppInfo appInfo() {
        try {
            LOGGER.info("Initializing Application Info");
            AppInfo applicationInfo = loadApplicationInfo();
            if (applicationInfo.getName() == null) {
                applicationInfo.setName(environment.getProperty("spring.application.name"));
            }
            if (applicationInfo.getName() == null || applicationInfo.getName().isEmpty()) {
                applicationInfo.setName("ATS App");
            }


            return applicationInfo;
        } catch (Exception e) {
            LOGGER.error("ERROR LOADING APP INFO");
            return null;
        }
    }

    static AppInfo loadApplicationInfo() {
        PropertiesConfiguration external = null;
        try {

            external = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(new Parameters().properties().setFileName("META-INF/applicationInfo.properties")
                            .setThrowExceptionOnMissing(true)
                            .setListDelimiterHandler(new DefaultListDelimiterHandler(',')).setIncludesAllowed(false)
                    ).getConfiguration();
        } catch (ConfigurationException e) {
            LOGGER.warn("WARNING -- NOT FOUND applicationInfo.properties");
        }

        Properties pro = new Properties();
        Iterator<String> keys = external.getKeys();
        PropertiesConfiguration finalExternal = external;
        keys.forEachRemaining(e -> pro.setProperty(e, finalExternal.getString(e)));

        AppInfo applicationInfo = AppInfo.load(pro);
        return applicationInfo;
    }
}
