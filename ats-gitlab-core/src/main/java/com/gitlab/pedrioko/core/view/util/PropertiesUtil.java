package com.gitlab.pedrioko.core.view.util;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.OverrideCombiner;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.createTempFile;

@Component
public class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    private CombinedConfiguration config;

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
    }

    public List<String> getFieldTable(Class<?> c) {
        return getListClass(c, ".table");
    }

    public List<String> getFieldForm(Class<?> c) {
        return getListClass(c, ".form");
    }

    public List<String> getListClass(Class<?> c, String add) {
        return config.getList(c.getSimpleName() + add).stream().map(Object::toString).collect(Collectors.toList());
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
        return config.getBoolean(key);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getBoolean(java.lang.String,
     * boolean)
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getBoolean(java.lang.String,
     * java.lang.Boolean)
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    /**
     * @param key
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String)
     */
    public double getDouble(String key) {
        return config.getDouble(key);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String, double)
     */
    public double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String, java.lang.Double)
     */
    public Double getDouble(String key, Double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

}
