package com.gitlab.pedrioko.services;


import com.gitlab.pedrioko.core.lang.AppParam;

public interface ParamService {

    AppParam getParam(String name);

    String getParamValue(String name);

    AppParam saveParam(AppParam param);

    AppParam saveParam(String name, String value);
}
