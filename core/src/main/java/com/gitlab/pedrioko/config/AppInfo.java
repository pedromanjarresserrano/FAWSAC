package com.gitlab.pedrioko.config;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public @Data
class AppInfo implements Serializable {

    private Map<String, String> properties;
    private Map<String, String> systemProperties;
    private Map<String, String> systemEnvironment;

    private AppInfo() {
        systemProperties = new HashMap(System.getProperties());
        systemEnvironment = System.getenv();
        properties = new HashMap<>();
        properties.put("template", "Default");
    }

    public static AppInfo load(Properties properties) {
        AppInfo appinfo = new AppInfo();
        properties.stringPropertyNames().forEach(key -> {
            try {
                String value = properties.getProperty(key);
                appinfo.addProperty(key, value);
            } catch (Exception e) {
            }

        });
        return appinfo;
    }

    public String getDefaultLogo() {
        return properties.get("defaultLogo");
    }

    public String getDefaultIcon() {
        return properties.get("defaultIcon");
    }

    public String getBuild() {
        return properties.get("build");
    }

    public String getVersion() {
        return properties.get("version");
    }


    public String getDescription() {
        return properties.get("description");
    }

    public String getAuthor() {
        return properties.get("author");
    }

    public String getCompany() {
        return properties.get("company");
    }

    public String getUrl() {
        return properties.get("url");
    }


    public String getName() {
        return properties.get("name");
    }

    public void setName(String name) {
        properties.put("name", name);
    }

    public String getShortName() {
        return properties.get("shortName");
    }

    public void setShortName(String shortName) {
        addProperty("shortName", shortName);
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }
}
