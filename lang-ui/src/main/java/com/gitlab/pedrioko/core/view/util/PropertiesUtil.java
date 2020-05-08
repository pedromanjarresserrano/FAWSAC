package com.gitlab.pedrioko.core.view.util;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.nio.file.Files.createTempFile;

@Component
public class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    //  private CombinedConfiguration config;
    private JSONObject jsonproperties = new JSONObject();

    public PropertiesUtil() {

        Object jsonInternal = null;
        try {
            JSONParser jsonParser = new JSONParser();
            InputStream resourceAsStream = PropertiesUtil.class.getResourceAsStream("/internaldomain.json");
            File destination = createTempFile("temp", "temp").toFile();
            FileUtils.copyInputStreamToFile(resourceAsStream, destination);
            FileReader reader = new FileReader(destination);
            jsonInternal = jsonParser.parse(reader);

        } catch (Exception e) {
            LOGGER.warn("WARNING -- NOT FOUND internaldomain.json", e);
        }
        Object jsonExternal = null;
        try {
            JSONParser jsonParser = new JSONParser();
            InputStream resourceAsStream = PropertiesUtil.class.getResourceAsStream("/domain.json");
            File destination = createTempFile("temp", "temp").toFile();
            FileUtils.copyInputStreamToFile(resourceAsStream, destination);
            FileReader reader = new FileReader(destination);
            jsonExternal = jsonParser.parse(reader);

        } catch (Exception e) {
            LOGGER.warn("WARNING -- NOT FOUND domain.json", e);
        }

        JSONObject[] objs = new JSONObject[]{(JSONObject) jsonInternal, (JSONObject) jsonExternal};
        for (JSONObject obj : objs) {
            if (obj != null) {
                Set set = obj.keySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    jsonproperties.put(key, obj.get(key));
                }
            }
        }
    }

    public JSONArray getFieldTable(Class<?> c) {
        return getListClass(c, ".table.fields");
    }

    public String getCrudViewPartPosition(Class<?> c, String part) {
        return getString(c + ".crudview." + part);
    }

    public JSONArray getFieldForm(Class<?> c) {
        return getListClass(c, ".form.fields");
    }

    public Object getJsonValue(String key) {
        Object a = this.jsonproperties;
        String[] split = key.split("\\.");
        for (String k : split) {
            if (a == null) return null;
            a = a instanceof JSONObject ? ((JSONObject) a).get(k) : a instanceof JSONArray ? ((JSONArray) a) : a;
        }
        return a;
    }

    public JSONArray getListClass(Class<?> c, String add) {
        String key = c.getSimpleName() + add;
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null && !((List) jsonValue).isEmpty()) {
            return (JSONArray) jsonValue;
        } else {
            return new JSONArray();
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



    public boolean getBoolean(String key) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null) {
            return (boolean) jsonValue;
        } else {
            return true;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null) {
            return (boolean) jsonValue;
        } else {
            return defaultValue;
        }
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null) {
            return (Boolean) jsonValue;
        } else {
            return defaultValue;
        }
    }

    public double getDouble(String key) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null) {
            return (Double) jsonValue;
        } else {
            return -1.0D;
        }
    }


    public double getDouble(String key, double defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null) {
            return (Double) jsonValue;
        } else {
            return defaultValue;
        }
    }

    public Double getDouble(String key, Double defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null) {
            return (Double) jsonValue;
        } else {
            return defaultValue;
        }
    }
    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        Object jsonValue = getJsonValue(key);
        if (jsonValue != null) {
            return (String) jsonValue;
        } else {
            return defaultValue;
        }
    }

}
