package com.gitlab.pedrioko.core.view.util;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.OverrideCombiner;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.createTempFile;

@Component
public class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    private CombinedConfiguration config;
    private Object jsoNfile;

    public PropertiesUtil() {
        PropertiesConfiguration external = null;

        try {

            external = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(new Parameters().properties().setFileName("domain.properties")
                            .setThrowExceptionOnMissing(true)
                            .setListDelimiterHandler(new DefaultListDelimiterHandler(',')).setIncludesAllowed(false)
                    ).getConfiguration();
        } catch (ConfigurationException e) {
            LOGGER.warn("WARNING -- NOT FOUND domain.properties");
        }
        PropertiesConfiguration internal = null;
        try {
            InputStream resourceAsStream = PropertiesUtil.class.getResourceAsStream("/internaldomain.properties");
            File destination = createTempFile("temp", "temp").toFile();
            FileUtils.copyInputStreamToFile(resourceAsStream, destination);
            internal = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(new Parameters().properties().setFile(destination)
                            .setThrowExceptionOnMissing(true)
                            .setListDelimiterHandler(new DefaultListDelimiterHandler(',')).setIncludesAllowed(false)
                    ).getConfiguration();
        } catch (ConfigurationException | IOException e) {
            LOGGER.warn("WARNING -- NOT FOUND internaldomain.properties");
        }
        CombinedConfiguration config = new CombinedConfiguration(new OverrideCombiner());
        if (external != null) {
            config.addConfiguration(external);//this overrides config2
        }
        config.addConfiguration(internal);
        this.config = config;
        try {
            JSONParser jsonParser = new JSONParser();
            InputStream resourceAsStream = PropertiesUtil.class.getResourceAsStream("/internaldomain.json");
            File destination = createTempFile("temp", "temp").toFile();
            FileUtils.copyInputStreamToFile(resourceAsStream, destination);
            FileReader reader = new FileReader(destination);
            jsoNfile = jsonParser.parse(reader);
        } catch (Exception e) {
            LOGGER.warn("WARNING -- NOT FOUND internaldomain.json", e);
        }
    }

    public List<String> getFieldTable(Class<?> c) {
        return getListClass(c, ".table.fields");
    }

    public List<String> getFieldForm(Class<?> c) {
        return getListClass(c, ".form.fields");
    }

    public Object getJsonValue(String key) {
        Object a = this.jsoNfile;
        String[] split = key.split("\\.");
        for (String k : split) {
            a = a instanceof JSONObject ? ((JSONObject) a).get(k) : ((JSONArray) a);
        }
        return a;
    }

    public List<String> getListClass(Class<?> c, String add) {
        String key = c.getSimpleName() + add;
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null && !((List) jsonValue).isEmpty()) {
            return (List) jsonValue;
        } else {
            return config.getList(key).stream().map(Object::toString).collect(Collectors.toList());
        }
    }

    public boolean getEnableSubCrudsClass(Class<?> c, Boolean defaulvalue) {
        return getBoolean(c.getSimpleName() + ".table.subcruds", defaulvalue);
    }

    public boolean getEnableSubCrudsClassProperty(Class<?> c, String property, Boolean defaulvalue) {
        return getBoolean(c.getSimpleName() + ".table.subcruds." + StringUtil.getDescapitalize(property), defaulvalue);
    }

    public boolean getEnableCommonActionsClass(Class<?> c) {
        return getBoolean(c.getSimpleName() + ".table.commonactions.enable", true);
    }


    /**
     * @param key
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String key) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null && !((List) jsonValue).isEmpty()) {
            return (boolean) jsonValue;
        } else {
            return config.getBoolean(key);
        }
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getBoolean(java.lang.String,
     * boolean)
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null && !((List) jsonValue).isEmpty()) {
            return (boolean) jsonValue;
        } else {
            return config.getBoolean(key, defaultValue);
        }
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getBoolean(java.lang.String,
     * java.lang.Boolean)
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null && !((List) jsonValue).isEmpty()) {
            return (Boolean) jsonValue;
        } else {
            return config.getBoolean(key, defaultValue);
        }
    }

    /**
     * @param key
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String)
     */
    public double getDouble(String key) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null && !((List) jsonValue).isEmpty()) {
            return (Double) jsonValue;
        } else {
            return config.getDouble(key);
        }
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String, double)
     */
    public double getDouble(String key, double defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null && !((List) jsonValue).isEmpty()) {
            return (Double) jsonValue;
        } else {
            return config.getDouble(key, defaultValue);
        }
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String, java.lang.Double)
     */
    public Double getDouble(String key, Double defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null && !((List) jsonValue).isEmpty()) {
            return (Double) jsonValue;
        } else {
            return config.getDouble(key, defaultValue);
        }
    }

}
